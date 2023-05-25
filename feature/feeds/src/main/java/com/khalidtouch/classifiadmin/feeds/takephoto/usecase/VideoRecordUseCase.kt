package com.khalidtouch.classifiadmin.feeds.takephoto.usecase

import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import javax.inject.Inject

class VideoRecordUseCase @Inject constructor() {
    operator fun invoke(): VideoCapture<Recorder> {
        val selector = QualitySelector.from(
            Quality.HD,
            FallbackStrategy.higherQualityOrLowerThan(Quality.SD)
        )

        val recorder = Recorder.Builder()
            .setQualitySelector(selector)
            .build()

        return VideoCapture.withOutput(recorder)
    }
}

fun QualitySelector.getResolutions(
    selector: CameraSelector,
    provider: ProcessCameraProvider,
): Map<Quality, Size> = selector.filter(provider.availableCameraInfos)
    .firstOrNull()
    ?.let { cameraInfo ->
        QualitySelector.getSupportedQualities(cameraInfo)
            .associateWith { quality ->
                QualitySelector.getResolution(cameraInfo, quality)!!
            }
    } ?: emptyMap()