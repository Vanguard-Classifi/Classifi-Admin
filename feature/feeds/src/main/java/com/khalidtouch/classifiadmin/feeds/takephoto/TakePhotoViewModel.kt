package com.khalidtouch.classifiadmin.feeds.takephoto

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.util.concurrent.ListenableFuture
import com.khalidtouch.chatme.datastore.ClassifiPreferencesDataSource
import com.khalidtouch.classifiadmin.feeds.takephoto.usecase.CameraPreviewUseCase
import com.khalidtouch.classifiadmin.feeds.takephoto.usecase.ImageCaptureUseCase
import com.khalidtouch.classifiadmin.model.FeedMessage
import com.khalidtouch.classifiadmin.model.MessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@HiltViewModel
class TakePhotoViewModel @Inject constructor(
    imageCaptureUseCase: ImageCaptureUseCase,
    cameraPreviewUseCase: CameraPreviewUseCase,
    private val prefDataSource: ClassifiPreferencesDataSource,
    @SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) : ViewModel() {
    val TAG = "TakePhoto"
    private var _cameraSelector = MutableStateFlow(CameraSelector.DEFAULT_BACK_CAMERA)
    val cameraSelector: StateFlow<CameraSelector> = _cameraSelector

    val cameraFlipState: StateFlow<Boolean> = _cameraSelector.map {
        it == CameraSelector.DEFAULT_BACK_CAMERA
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = true,
    )

    private val _flashlightState = MutableStateFlow<Boolean>(false)
    val flashlightState: StateFlow<Boolean> = _flashlightState

    private val _cameraUseState = MutableStateFlow<CameraUseState>(CameraUseState.Photo)
    val cameraUseState: StateFlow<CameraUseState> = _cameraUseState

    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    @SuppressLint("StaticFieldLeak")
    val preview: PreviewView = PreviewView(context).apply {
        this.scaleType = PreviewView.ScaleType.FILL_CENTER
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private val imageCaptureUseCase = imageCaptureUseCase(_flashlightState.value)
    private val cameraPreviewUseCase = cameraPreviewUseCase(preview)

    private val emptyImageUri: Uri = Uri.parse("file://dev/null")
    private val _imageUri = MutableStateFlow<Uri>(emptyImageUri)
    val imageUri: StateFlow<Uri> = _imageUri

    val hasNoSavedImage: StateFlow<Boolean> =
        combine(
            _imageUri,
            flowOf(emptyImageUri)
        ) { image, emptyImage ->
            image == emptyImage
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = false,
        )

    fun onSavePhotoFile(newUri: Uri) {
        _imageUri.value = newUri
    }

    fun onSavePhotoFile(file: File) = viewModelScope.launch {
        _imageUri.value = file.toUri()
        if (_imageUri.value != emptyImageUri) {
            try {
                prefDataSource.userData.collect {
                    val id = it.feedData.messages.size.toLong().inc()
                    Log.e(TAG, "onSavePhotoFile: the id is $id")
                    prefDataSource.enqueueNonTextMessage(
                        FeedMessage(
                            messageId = id,
                            uri = _imageUri.value.toString(),
                            feedType = MessageType.ImageMessage
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cancelPhotoCapture() {
        _imageUri.value = emptyImageUri
    }

    fun onToggleFlashlight(flashlightState: Boolean) {
        _flashlightState.value = !flashlightState
    }

    fun onToggleCamera(cameraFlipState: Boolean) {
        val selector = when (cameraFlipState) {
            true -> CameraSelector.DEFAULT_FRONT_CAMERA
            false -> CameraSelector.DEFAULT_BACK_CAMERA
        }
        _cameraSelector.value = selector
    }

    fun onToggleCameraUseState(state: CameraUseState) {
        val newState = when (state) {
            CameraUseState.Photo -> CameraUseState.Video
            CameraUseState.Video -> CameraUseState.Photo
        }
        _cameraUseState.value = newState
    }

    fun onInitializeCamera(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            val cameraProvider = context.getCameraProvider()
            try {
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    _cameraSelector.value,
                    cameraPreviewUseCase,
                    imageCaptureUseCase,
                )

                val cameraControl = camera.cameraControl
                val torch: ListenableFuture<Void> =
                    cameraControl.enableTorch(_flashlightState.value)
                torch.addListener({
                    try {
                        torch.get()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, cameraExecutor)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onTakeSnapshot(callback: (File) -> Unit) {
        viewModelScope.launch {
            imageCaptureUseCase.takeSnapshot(cameraExecutor)
                .let(callback)
        }

    }
}

val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)

suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { future ->
            future.addListener({
                continuation.resume(future.get())
            }, executor)
        }
    }


suspend fun ImageCapture.takeSnapshot(executor: Executor): File {
    val photo = withContext(Dispatchers.IO) {
        kotlin.runCatching {
            File.createTempFile("image", "jpg")
        }.getOrElse { e ->
            e.printStackTrace()
            File("/dev/null")
        }
    }

    return suspendCoroutine { continuation ->
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photo).build()
        takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                continuation.resume(photo)
            }

            override fun onError(exception: ImageCaptureException) {
                continuation.resumeWithException(exception)
            }
        })
    }
}