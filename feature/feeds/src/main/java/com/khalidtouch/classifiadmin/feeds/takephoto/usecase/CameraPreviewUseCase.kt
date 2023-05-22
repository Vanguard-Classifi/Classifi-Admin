package com.khalidtouch.classifiadmin.feeds.takephoto.usecase

import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import javax.inject.Inject

class CameraPreviewUseCase @Inject constructor() {
    operator fun invoke(view: PreviewView) = Preview.Builder()
        .build()
        .also { it.setSurfaceProvider(view.surfaceProvider) }
}