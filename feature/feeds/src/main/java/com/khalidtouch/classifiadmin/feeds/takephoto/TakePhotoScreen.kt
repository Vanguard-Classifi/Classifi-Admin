package com.khalidtouch.classifiadmin.feeds.takephoto

import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel
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
    val hasNoSavedImage by takePhotoViewModel.hasNoSavedImage.collectAsStateWithLifecycle()
    val imageUri by takePhotoViewModel.imageUri.collectAsStateWithLifecycle()

    Log.e(TAG, "TakePhotoRoute: imageUri is currently ${imageUri.toString()}" )

    if (hasNoSavedImage)
        TakePhotoScreen(
            onDismissDialog = onDismissDialog,
            onViewAlbum = onViewAlbum,
            onCloseCameraPreview = onClose,
            onSavePhotoFile = takePhotoViewModel::onSavePhotoFile
        )

    AnimatedVisibility(
        visible = !hasNoSavedImage,
        enter = slideInHorizontally(
            initialOffsetX = { fullHeight -> -fullHeight },
        ) + fadeIn(),
        exit = slideOutHorizontally(
            targetOffsetX = { fullHeight -> -fullHeight },
        ) + fadeOut(),
    ) {
        PhotoCaptureScreen(
            onNext = onNext,
            onCancel = takePhotoViewModel::cancelPhotoCapture,
            imageUri = imageUri,
        )
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun TakePhotoScreen(
    takePhotoViewModel: TakePhotoViewModel = hiltViewModel<TakePhotoViewModel>(),
    cameraPermissionState: PermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    ),
    onDismissDialog: () -> Unit,
    onViewAlbum: () -> Unit,
    onCloseCameraPreview: () -> Unit,
    onSavePhotoFile: (File) -> Unit,
) {
    val flashlightState by takePhotoViewModel.flashlightState.collectAsStateWithLifecycle()
    val cameraFlipState by takePhotoViewModel.cameraFlipState.collectAsStateWithLifecycle()
    val cameraUseState by takePhotoViewModel.cameraUseState.collectAsStateWithLifecycle()

    TakePhotoScreen(
        flashlightState = flashlightState,
        cameraFlipState = cameraFlipState,
        cameraUseState = cameraUseState,
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
            when (cameraUseState) {
                CameraUseState.Photo -> {
                    takePhotoViewModel.onTakeSnapshot(onSavePhotoFile)
                }

                CameraUseState.Video -> {
                    /*todo -- video capture */
                }
            }
        }
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun TakePhotoScreen(
    flashlightState: Boolean,
    cameraFlipState: Boolean,
    cameraUseState: CameraUseState,
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
    content: @Composable () -> Unit = {
        CameraPreviewContent(
            flashlightState = flashlightState,
            cameraFlipState = cameraFlipState,
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

    LaunchedEffect(flashlightState) {
        onInitializeCamera(lifecycleOwner)
    }

    BoxWithConstraints {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        if (!cameraPermissionState.status.isGranted) {
            noPermissionScreen()
        } else {
            content()
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


sealed interface CameraUseState {
    object Photo : CameraUseState
    object Video : CameraUseState
}