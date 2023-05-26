package com.khalidtouch.classifiadmin.feeds.takephoto

import android.content.Context
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.khalidtouch.core.designsystem.icons.ClassifiIcons


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TakePhotoRoute(
    onDismissDialog: () -> Unit,
    onClose: () -> Unit,
    onNext: () -> Unit,
    onViewAlbum: () -> Unit,
    takePhotoViewModel: TakePhotoViewModel = hiltViewModel<TakePhotoViewModel>(),
) {
    val TAG = "TakePhoto"
    val uiState by takePhotoViewModel.uiState.collectAsStateWithLifecycle()


    TakePhotoScreen(
        uiState = uiState,
        onDismissDialog = onDismissDialog,
        onViewAlbum = onViewAlbum,
        onCloseCameraPreview = onClose,
    )

    PhotoCaptureScreen(
        onNext = {
            onNext().let { takePhotoViewModel.enqueuePhotoFileForPosting() }
            Log.e(TAG, "TakePhotoRoute: enqueuePhotoFileForPosting")
        },
        onCancel = takePhotoViewModel::cancelPhotoCapture,
        uiState = uiState
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun TakePhotoScreen(
    uiState: TakePhotoUiState,
    takePhotoViewModel: TakePhotoViewModel = hiltViewModel<TakePhotoViewModel>(),
    cameraPermissionState: PermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    ),
    audioPermissionState: PermissionState = rememberPermissionState(
        android.Manifest.permission.RECORD_AUDIO
    ),
    onDismissDialog: () -> Unit,
    onViewAlbum: () -> Unit,
    onCloseCameraPreview: () -> Unit,
) {
    TakePhotoScreen(
        uiState = uiState,
        cameraPermissionState = cameraPermissionState,
        audioPermissionState = audioPermissionState,
        onToggleFlashlight = takePhotoViewModel::onToggleFlashlight,
        onToggleCameraUseState = takePhotoViewModel::onToggleCameraUseState,
        onToggleCamera = takePhotoViewModel::onToggleCamera,
        onDismissDialog = onDismissDialog,
        onRequestCameraPermission = cameraPermissionState::launchPermissionRequest,
        onRequestAudioPermission = audioPermissionState::launchPermissionRequest,
        onCloseCameraPreview = onCloseCameraPreview,
        onViewAlbum = onViewAlbum,
        previewView = takePhotoViewModel.preview,
        onInitializeCamera = takePhotoViewModel::onInitializeCamera,
        onPrepareSnapshot = takePhotoViewModel::onPrepareSnapshot,
        onPrepareVideoRecord = takePhotoViewModel::onPrepareVideoRecord,
        switchToCameraMode = takePhotoViewModel::switchToCameraMode,
        onStopVideoRecording = takePhotoViewModel::onStopVideoRecording,
        onToggleVideoPauseState = takePhotoViewModel::onToggleVideoPauseState,
        onPauseVideo = takePhotoViewModel::onPauseVideo,
        onResumeVideo = takePhotoViewModel::onResumeVideo,
        onSaveVideoRecording = takePhotoViewModel::onSaveVideoRecording
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun TakePhotoScreen(
    uiState: TakePhotoUiState,
    previewView: PreviewView,
    context: Context = LocalContext.current,
    cameraPermissionState: PermissionState,
    audioPermissionState: PermissionState,
    onRequestCameraPermission: () -> Unit,
    onRequestAudioPermission: () -> Unit,
    onDismissDialog: () -> Unit,
    onViewAlbum: () -> Unit,
    switchToCameraMode: () -> Unit,
    onToggleVideoPauseState: (Boolean) -> Unit,
    onStopVideoRecording: () -> Unit,
    onCloseCameraPreview: () -> Unit,
    onToggleFlashlight: (Boolean) -> Unit,
    onToggleCamera: (Boolean) -> Unit,
    onPrepareSnapshot: () -> Unit,
    onPauseVideo: () -> Unit,
    onResumeVideo: () -> Unit,
    onSaveVideoRecording: () -> Unit,
    onPrepareVideoRecord: (Context) -> Unit,
    onToggleCameraUseState: (CameraUseState) -> Unit,
    onInitializeCamera: (LifecycleOwner) -> Unit,
    noPermissionScreen: @Composable () -> Unit = {
        NoPermissionScreen(
            onRequestCameraPermission = onRequestCameraPermission,
            onDismissDialog = onDismissDialog,
            onRequestAudioPermission = onRequestAudioPermission,
            cameraPermissionState = cameraPermissionState,
            audioPermissionState = audioPermissionState,
        )
    },
    photoContent: @Composable (
        flashlightState: Boolean,
        isRearCameraActive: Boolean,
        cameraUseState: CameraUseState,
    ) -> Unit = { flashlight, rearCamera, cameraUseState ->
        CameraPreviewContent(
            flashlightState = flashlight,
            isRearCameraActive = rearCamera,
            onPrepareCamera = {
                when (uiState) {
                    is TakePhotoUiState.Loading -> Unit
                    is TakePhotoUiState.Loaded -> {
                        when (uiState.data.cameraState.cameraUseState) {
                            CameraUseState.PhotoIdle -> {
                                onPrepareSnapshot()
                            }

                            CameraUseState.VideoIdle -> {
                                onPrepareVideoRecord(context)
                            }

                            else -> Unit
                        }
                    }
                }
            },
            onToggleCamera = onToggleCamera,
            onCloseCameraPreview = onCloseCameraPreview,
            onViewAlbum = onViewAlbum,
            onToggleCameraUseState = onToggleCameraUseState,
            onToggleFlashlight = onToggleFlashlight,
            cameraUseState = cameraUseState,
        )
    },
    videoContent: @Composable () -> Unit = {
        VideoRecordContent(
            isVideoRecordPaused = when (uiState) {
                is TakePhotoUiState.Loading -> false
                is TakePhotoUiState.Loaded -> {
                    uiState.data.cameraState.cameraUseState ==
                            CameraUseState.VideoPaused
                }
            },
            switchToCameraMode = switchToCameraMode,
            onToggleVideoPauseState = onToggleVideoPauseState,
            onStopVideoRecording = onStopVideoRecording,
        )
    },
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val TAG = "TakePhoto"

    LaunchedEffect(uiState) {
        onInitializeCamera(lifecycleOwner)
    }

    DisposableEffect(uiState) {
        when (uiState) {
            is TakePhotoUiState.Loading -> Unit
            is TakePhotoUiState.Loaded -> {
                when (uiState.data.cameraState.cameraUseState) {
                    CameraUseState.VideoPaused -> {
                        onPauseVideo()
                    }

                    CameraUseState.VideoResumed -> {
                        onResumeVideo()
                    }

                    CameraUseState.VideoRecordingStopped -> {
                        onSaveVideoRecording()
                    }

                    else -> Unit
                }
            }
        }
        onDispose { }
    }


    when (uiState) {
        is TakePhotoUiState.Loading -> Unit
        is TakePhotoUiState.Loaded -> {
            if (uiState.data.hasNoSavedMedia) {
                BoxWithConstraints {
                    AndroidView(
                        factory = { previewView },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (
                        !cameraPermissionState.status.isGranted ||
                        !audioPermissionState.status.isGranted
                    ) {
                        noPermissionScreen()
                    } else {
                        when (uiState.data.cameraState.cameraUseState) {
                            CameraUseState.PhotoIdle,
                            CameraUseState.VideoIdle,
                            CameraUseState.PhotoTakeSnapshot -> {
                                photoContent(
                                    flashlightState = uiState.data.cameraState.isFlashlightOn,
                                    isRearCameraActive = uiState.data.cameraState.isRearCameraActive,
                                    cameraUseState = uiState.data.cameraState.cameraUseState,
                                )
                            }

                            CameraUseState.VideoRecording,
                            CameraUseState.VideoResumed,
                            CameraUseState.VideoPaused -> {
                                videoContent()
                            }

                            else -> Unit
                        }
                    }
                }
            }
        }
    }
}


enum class CameraToggleFeature(
    val uncheckedIcon: Int,
    val checkedIcon: Int
) {
    Flashlight(ClassifiIcons.FlashOff, ClassifiIcons.FlashOn),
    FlipCamera(ClassifiIcons.FlipCamera, ClassifiIcons.FlipCamera),
}


