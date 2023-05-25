package com.khalidtouch.classifiadmin.feeds.takephoto

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.khalidtouch.core.designsystem.components.ClassifiIconButton
import com.khalidtouch.core.designsystem.components.ClassifiTakeSnapshotButton
import com.khalidtouch.core.designsystem.components.ClassifiToggleButton
import com.khalidtouch.core.designsystem.icons.ClassifiIcons

@Composable
fun VideoRecordContent(
    isVideoRecordPaused: Boolean,
    switchToCameraMode: () -> Unit,
    onToggleVideoPauseState: (Boolean) -> Unit,
    onStopVideoRecording: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(
                top = ClassifiCameraDefaults.edgePadding.times(3f),
                start = ClassifiCameraDefaults.edgePadding,
                end = ClassifiCameraDefaults.edgePadding,
            ), contentAlignment = Alignment.TopCenter
    ) {
        //timer button 
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = ClassifiCameraDefaults.edgePadding.times(3f)),
        contentAlignment = Alignment.BottomCenter
    ) {
        VideoRecordBottomButtons(
            recordStopButton = {
                ClassifiTakeSnapshotButton(
                    icon = {
                        Icon(
                            painterResource(id = ClassifiIcons.Stop),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                        )
                    },
                    onClick = onStopVideoRecording,
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.error,
                    ),
                    color = Color.Transparent,
                )
            },
            pauseResumeButton = {
                ClassifiToggleButton(
                    checked = isVideoRecordPaused,
                    onCheckedChange = onToggleVideoPauseState,
                    enabled = true,
                    icon = {
                        Icon(
                            painterResource(id = ClassifiIcons.Pause),
                            contentDescription = null
                        )
                    },
                    checkedIcon = {
                        Icon(
                            painterResource(id = ClassifiIcons.Play),
                            contentDescription = null
                        )
                    },
                )
            },
            cameraButton = {
                ClassifiIconButton(
                    onClick = switchToCameraMode,
                    icon = {
                        Icon(
                            painter = painterResource(id = ClassifiIcons.Camera),
                            contentDescription = null
                        )
                    }
                )
            }
        )
    }
}


@Composable
fun VideoRecordBottomButtons(
    recordStopButton: @Composable () -> Unit,
    pauseResumeButton: @Composable () -> Unit,
    cameraButton: @Composable () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            ), horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box { cameraButton() }
        Box { recordStopButton() }
        Box { pauseResumeButton() }
    }
}
