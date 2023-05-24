package com.khalidtouch.classifiadmin.feeds.takephoto

import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import java.io.File


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
        onSavePhotoFile = takePhotoViewModel::onSavePhotoFile
    )

    PhotoCaptureScreen(
        onNext = onNext,
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
    onDismissDialog: () -> Unit,
    onViewAlbum: () -> Unit,
    onCloseCameraPreview: () -> Unit,
    onSavePhotoFile: (File) -> Unit,
) {

    TakePhotoScreen(
        uiState = uiState,
        cameraPermissionState = cameraPermissionState,
        onToggleFlashlight = takePhotoViewModel::onToggleFlashlight,
        onToggleCameraUseState = takePhotoViewModel::onToggleCameraUseState,
        onToggleCamera = takePhotoViewModel::onToggleCamera,
        onDismissDialog = onDismissDialog,
        onRequestCameraPermission = cameraPermissionState::launchPermissionRequest,
        onCloseCameraPreview = onCloseCameraPreview,
        onViewAlbum = onViewAlbum,
        previewView = takePhotoViewModel.preview,
        onInitializeCamera = takePhotoViewModel::onInitializeCamera,
        onEngageCamera = {
            when (uiState) {
                is TakePhotoUiState.Loading -> Unit
                is TakePhotoUiState.Loaded -> {
                    when (uiState.data.cameraState.cameraUseState) {
                        CameraUseState.Photo -> {
                            takePhotoViewModel.onTakeSnapshot(onSavePhotoFile)
                        }

                        CameraUseState.Video -> {
                            /*todo -- video capture */
                        }
                    }
                }
            }
        }
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun TakePhotoScreen(
    uiState: TakePhotoUiState,
    previewView: PreviewView,
    cameraPermissionState: PermissionState,
    onRequestCameraPermission: () -> Unit,
    onDismissDialog: () -> Unit,
    onViewAlbum: () -> Unit,
    onCloseCameraPreview: () -> Unit,
    onToggleFlashlight: (Boolean) -> Unit,
    onToggleCamera: (Boolean) -> Unit,
    onEngageCamera: () -> Unit,
    onToggleCameraUseState: (CameraUseState) -> Unit,
    onInitializeCamera: (LifecycleOwner) -> Unit,
    noPermissionScreen: @Composable () -> Unit = {
        NoPermissionScreen(
            onRequestCameraPermission = onRequestCameraPermission,
            onDismissDialog = onDismissDialog
        )
    },
    content: @Composable (
        flashlightState: Boolean,
        isRearCameraActive: Boolean,
        cameraUseState: CameraUseState,
    ) -> Unit = { flashlight, rearCamera, cameraUseState ->
        CameraPreviewContent(
            flashlightState = flashlight,
            isRearCameraActive = rearCamera,
            onEngageCamera = onEngageCamera,
            onToggleCamera = onToggleCamera,
            onCloseCameraPreview = onCloseCameraPreview,
            onViewAlbum = onViewAlbum,
            onToggleCameraUseState = onToggleCameraUseState,
            onToggleFlashlight = onToggleFlashlight,
            cameraUseState = cameraUseState,
        )
    },
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(uiState) {
        onInitializeCamera(lifecycleOwner)
    }

    when (uiState) {
        is TakePhotoUiState.Loading -> Unit
        is TakePhotoUiState.Loaded -> {
            if (uiState.data.hasNoSavedMedia)
                BoxWithConstraints {
                    AndroidView(
                        factory = { previewView },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (!cameraPermissionState.status.isGranted) {
                        noPermissionScreen()
                    } else {
                        content(
                            flashlightState = uiState.data.cameraState.isFlashlightOn,
                            isRearCameraActive = uiState.data.cameraState.isRearCameraActive,
                            cameraUseState = uiState.data.cameraState.cameraUseState,
                        )
                    }
                }
        }
    }
}


enum class CameraToggleFeature(
    val uncheckedIcon: Int,
    val checkedIcon: Int
) {
    Flashlight(ClassifiIcons.FlashOn, ClassifiIcons.FlashOff),
    FlipCamera(ClassifiIcons.FlipCamera, ClassifiIcons.FlipCamera),
}


