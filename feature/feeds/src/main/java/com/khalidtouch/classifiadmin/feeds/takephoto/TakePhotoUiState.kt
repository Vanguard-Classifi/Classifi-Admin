package com.khalidtouch.classifiadmin.feeds.takephoto

import android.net.Uri
import androidx.camera.core.CameraSelector


sealed interface TakePhotoUiState {
    object Loading : TakePhotoUiState
    data class Loaded(val data: TakePhotoData) : TakePhotoUiState
}

data class TakePhotoData(
    val cameraState: CameraState,
    val mediaUri: Uri,
    val hasNoSavedMedia: Boolean,
)


data class CameraState(
    val type: CameraSelector,
    val isRearCameraActive: Boolean,
    val isFlashlightOn: Boolean,
    val cameraUseState: CameraUseState,
) {
    companion object {
        val Default = CameraState(
            type = CameraSelector.DEFAULT_BACK_CAMERA,
            isRearCameraActive = true,
            isFlashlightOn = false,
            cameraUseState = CameraUseState.PhotoIdle,
        )
    }
}


sealed interface CameraUseState {
    object PhotoIdle : CameraUseState
    object PhotoTakeSnapshot : CameraUseState
    object PhotoSaved : CameraUseState
    object VideoIdle : CameraUseState
    object VideoRecording : CameraUseState
    object VideoPaused : CameraUseState
    object VideoResumed : CameraUseState
    object VideoRecordingStopped : CameraUseState

}