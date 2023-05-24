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
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
    private val _flashlightState = MutableStateFlow<Boolean>(false)
    private val _cameraUseState = MutableStateFlow<CameraUseState>(CameraUseState.Photo)
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
    private val _mediaUri = MutableStateFlow<Uri>(emptyImageUri)
    private val _cameraState: StateFlow<CameraState> =
        combine(
            _cameraSelector,
            _flashlightState,
            _cameraUseState
        ) { cameraSelector, flashlightState, cameraUseState ->
            CameraState(
                type = cameraSelector,
                isRearCameraActive = cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA,
                isFlashlightOn = flashlightState,
                cameraUseState = cameraUseState,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = CameraState.Default
        )

    val uiState: StateFlow<TakePhotoUiState> =
        combine(
            _cameraState,
            _mediaUri,
        ) { cameraState, mediaUri ->
            TakePhotoUiState.Loaded(
                data = TakePhotoData(
                    cameraState = cameraState,
                    mediaUri = mediaUri,
                    hasNoSavedMedia = mediaUri == emptyImageUri
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = TakePhotoUiState.Loading
        )


    fun onSavePhotoFile(file: File) = viewModelScope.launch {
        _mediaUri.value = file.toUri()
    }

    fun cancelPhotoCapture() {
        _mediaUri.value = emptyImageUri
    }

    fun onToggleFlashlight(newFlashlightState: Boolean) {
        _flashlightState.value = newFlashlightState
    }

    fun onToggleCamera(isRearCameraActive: Boolean) {
        val selector = when (isRearCameraActive) {
            false -> CameraSelector.DEFAULT_FRONT_CAMERA
            true -> CameraSelector.DEFAULT_BACK_CAMERA
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