package com.khalidtouch.classifiadmin.feeds.takephoto

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.util.concurrent.ListenableFuture
import com.khalidtouch.chatme.datastore.ClassifiPreferencesDataSource
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.classifiadmin.feeds.takephoto.usecase.CameraPreviewUseCase
import com.khalidtouch.classifiadmin.feeds.takephoto.usecase.ImageCaptureUseCase
import com.khalidtouch.classifiadmin.feeds.takephoto.usecase.VideoRecordUseCase
import com.khalidtouch.classifiadmin.model.FeedMessage
import com.khalidtouch.classifiadmin.model.MessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
    videoRecordUseCase: VideoRecordUseCase,
    private val userDataRepository: UserDataRepository,
    @SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
) : ViewModel() {
    val TAG = "TakePhoto"

    private var _cameraSelector = MutableStateFlow(CameraSelector.DEFAULT_BACK_CAMERA)
    private val _flashlightState = MutableStateFlow<Boolean>(false)
    private val _cameraUseState = MutableStateFlow<CameraUseState>(CameraUseState.PhotoIdle)
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val videoRecorderExecutor: ExecutorService = Executors.newSingleThreadExecutor()

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
    private val videoRecordUseCase = videoRecordUseCase()
    private var videoRecorderJob: Job? = null
    private var photoEnqueueJob: Job? = null
    private var videoRecording: Recording? = null

    private val emptyImageUri: Uri = Uri.parse("file://dev/null")
    private val _mediaUri = MutableStateFlow<Uri>(emptyImageUri)
    private val _cameraState: StateFlow<CameraState> =
        combine(
            _cameraSelector,
            _flashlightState,
            _cameraUseState,
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

    fun enqueuePhotoFileForPosting() {
        if (photoEnqueueJob != null) return
        if (_cameraUseState.value != CameraUseState.PhotoSaved) return
        try {
            if (_mediaUri.value != emptyImageUri) {
                photoEnqueueJob = viewModelScope.launch {
                    userDataRepository.userData.map {
                        val id = it.feedData.messages.size.toLong()

                        userDataRepository.enqueueMediaMessage(
                            FeedMessage(
                                messageId = id,
                                uri = "https://www.petsworld.in/blog/wp-content/uploads/2014/09/funny-cat.jpg",
                                feedType = MessageType.ImageMessage,
                            )
                        )
                    }
                }
                Log.e(
                    TAG,
                    "enqueuePhotoFileForPosting: save uri is ${_mediaUri.value.toString()}"
                )
                Log.e(TAG, "enqueuePhotoFileForPosting: enqueuing photo")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            photoEnqueueJob = null
            _cameraUseState.value = CameraUseState.PhotoIdle
        }
    }

    fun cancelPhotoCapture() {
        _mediaUri.value = emptyImageUri
        _cameraUseState.value = CameraUseState.PhotoIdle
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
            CameraUseState.PhotoIdle -> CameraUseState.VideoIdle
            else -> CameraUseState.PhotoIdle
        }
        _cameraUseState.value = newState
    }

    fun switchToCameraMode() {
        _cameraUseState.value = CameraUseState.PhotoIdle
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
                    videoRecordUseCase,
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

    private fun onTakeSnapshot(callback: (File) -> Unit) {
        viewModelScope.launch {
            imageCaptureUseCase.takeSnapshot(cameraExecutor)
                .let(callback)
            _cameraUseState.value = CameraUseState.PhotoTakeSnapshot
        }
    }

    private fun onSavePhotoFile(file: File) = viewModelScope.launch {
        _mediaUri.value = file.toUri()
        _cameraUseState.value = CameraUseState.PhotoSaved
    }


    fun onPrepareSnapshot() {
        if (_cameraUseState.value != CameraUseState.PhotoIdle) return
        onTakeSnapshot { onSavePhotoFile(it) }
    }

    private fun onVideoRecordEvent(event: VideoRecordEvent) {
        val duration = event.recordingStats.recordedDurationNanos
        Log.e(TAG, "onVideoRecordEvent: current duration $duration")
        when (event) {
            is VideoRecordEvent.Start -> {
                Log.e(TAG, "onVideoRecordEvent: recording has started")
            }

            is VideoRecordEvent.Resume -> {
                Log.e(TAG, "onVideoRecordEvent: recording has resumed")
            }

            is VideoRecordEvent.Pause -> {
                Log.e(TAG, "onVideoRecordEvent: recording has paused")
            }

            is VideoRecordEvent.Finalize -> {
                Log.e(TAG, "onVideoRecordEvent: ${event.recordingStats.recordedDurationNanos}")
                Log.e(TAG, "onVideoRecordEvent: recording has finalized")
                if (event.hasError()) {
                    Log.e(TAG, "onVideoRecordEvent: ${event.error} and the cause is ${event.cause}")
                }
            }
        }
    }

    fun onPrepareVideoRecord(context: Context) {
        if (_cameraUseState.value != CameraUseState.VideoIdle) return
        onRecordVideo(context, this::onVideoRecordEvent)
    }

    private fun onRecordVideo(context: Context, onVideoRecordEvent: (VideoRecordEvent) -> Unit) {
        try {
            Log.e(TAG, "onRecordVideo: trying to record video")
            videoRecorderJob = viewModelScope.launch {
                videoRecording = videoRecordUseCase.recordVideo(
                    context,
                    videoRecorderExecutor,
                    onVideoRecordEvent
                )
                _cameraUseState.value = CameraUseState.VideoRecording
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onToggleVideoPauseState(state: Boolean) {
        if (videoRecording == null) return
        val videoState = when (state) {
            true -> CameraUseState.VideoPaused
            false -> CameraUseState.VideoResumed
        }
        _cameraUseState.value = videoState
    }


    fun onPauseVideo() {
        Log.e(TAG, "onPauseVideo: checking for null recorder")
        if (videoRecording == null) return
        if (_cameraUseState.value == CameraUseState.VideoPaused)
            videoRecording?.pause()
        Log.e(TAG, "onPauseVideo: called")
    }

    fun onResumeVideo() {
        Log.e(TAG, "onResumeVideo: checking for null recorder")
        if (videoRecording == null) return
        if (_cameraUseState.value == CameraUseState.VideoResumed)
            videoRecording?.resume()
        Log.e(TAG, "onResumeVideo: called")
    }

    fun onSaveVideoRecording() {
        if (videoRecording == null) return
        if (_cameraUseState.value == CameraUseState.VideoRecordingStopped)
            videoRecording?.stop()
        Log.e(TAG, "onSaveVideoRecording: called")
    }

    fun onStopVideoRecording() {
        _cameraUseState.value = CameraUseState.VideoRecordingStopped
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

suspend fun VideoCapture<Recorder>.recordVideo(
    context: Context,
    executor: Executor,
    onVideoRecordEvent: (VideoRecordEvent) -> Unit,
): Recording {
    val location = File(
        context.getExternalFilesDir(Environment.DIRECTORY_DCIM).toString() +
                File.separator + "Classifi"
    )
    if (!location.exists()) location.mkdir()
    val video = withContext(Dispatchers.IO) {
        File.createTempFile("video", ".mp4", location)
    }
    val fileOutputOptions = FileOutputOptions.Builder(video).build()

    //using MediaStore
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis().toString())
        put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Classifi")
        }
    }

    val mediaStoreOutputOptions = MediaStoreOutputOptions.Builder(
        context.contentResolver,
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    ).setContentValues(contentValues)
        .build()

    return this.output.prepareRecording(context, mediaStoreOutputOptions)
        .apply {
            if (PermissionChecker.checkSelfPermission(
                    context,
                    android.Manifest.permission.RECORD_AUDIO
                ) == PermissionChecker.PERMISSION_GRANTED
            ) {
                withAudioEnabled()
            }
        }.start(executor) { recordEvent ->
            onVideoRecordEvent(recordEvent)
        }
}