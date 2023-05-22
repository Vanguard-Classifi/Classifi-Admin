package com.khalidtouch.classifiadmin.feeds.takephoto.usecase

import android.annotation.SuppressLint
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.core.ImageCapture.FLASH_TYPE_ONE_SHOT_FLASH
import javax.inject.Inject

class ImageCaptureUseCase @Inject constructor() {
    @SuppressLint("RestrictedApi")
    operator fun invoke(flashState: Boolean) = ImageCapture.Builder()
        .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
        .setFlashType(FLASH_TYPE_ONE_SHOT_FLASH)
        .setFlashMode(
            when (flashState) {
                false -> FLASH_MODE_OFF
                true -> FLASH_MODE_ON
            }
        ).build()
}