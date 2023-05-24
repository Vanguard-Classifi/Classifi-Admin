package com.khalidtouch.classifiadmin.feeds.takephoto

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
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
fun CameraPreviewContent(
    onCloseCameraPreview: () -> Unit,
    onToggleCamera: (Boolean) -> Unit,
    onToggleFlashlight: (Boolean) -> Unit,
    isRearCameraActive: Boolean,
    flashlightState: Boolean,
    onEngageCamera: () -> Unit,
    onViewAlbum: () -> Unit,
    cameraUseState: CameraUseState,
    onToggleCameraUseState: (CameraUseState) -> Unit,
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
        //top buttons
        CameraTopButtons(
            closeButton = {
                ClassifiIconButton(
                    onClick = onCloseCameraPreview,
                    icon = {
                        Icon(
                            painter = painterResource(id = ClassifiIcons.Close),
                            contentDescription = null
                        )
                    },
                )
            },
            cameraToggleButtons = {
                CameraToggleFeature.values().map { feature ->
                    ClassifiToggleButton(
                        colors = IconButtonDefaults.filledIconToggleButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            checkedContainerColor = Color.Transparent,
                            checkedContentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        checked = when (feature) {
                            CameraToggleFeature.Flashlight -> {
                                flashlightState
                            }

                            CameraToggleFeature.FlipCamera -> {
                                isRearCameraActive
                            }
                        },
                        onCheckedChange = {
                            when (feature) {
                                CameraToggleFeature.FlipCamera -> {
                                    onToggleCamera(it)
                                }

                                CameraToggleFeature.Flashlight -> {
                                    onToggleFlashlight(it)
                                }
                            }
                        },
                        checkedIcon = {
                            Icon(
                                painterResource(id = feature.checkedIcon),
                                contentDescription = feature.name,
                            )
                        },
                        icon = {
                            Icon(
                                painterResource(id = feature.uncheckedIcon),
                                contentDescription = feature.name,
                            )
                        },
                        enabled = true,
                    )
                }
            }
        )
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = ClassifiCameraDefaults.edgePadding.times(3f)),
        contentAlignment = Alignment.BottomCenter
    ) {
        //bottom buttons
        CameraBottomButtons(
            snapshotButton = {
                ClassifiTakeSnapshotButton(
                    onClick = onEngageCamera,
                    buttonSize = 72.dp,
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.background.copy(0.5f),
                    ),
                    color = when (cameraUseState) {
                        CameraUseState.Video -> {
                            MaterialTheme.colorScheme.error
                        }
                        CameraUseState.Photo -> {
                            MaterialTheme.colorScheme.tertiary
                        }
                    },
                    icon = {
                        Icon(
                            painterResource(
                                id = when (cameraUseState) {
                                    is CameraUseState.Photo -> {
                                        ClassifiIcons.Snapshot
                                    }

                                    is CameraUseState.Video -> {
                                        ClassifiIcons.VideoCamera
                                    }
                                }
                            ),
                            contentDescription = null
                        )
                    }
                )
            },
            videoButton = {
                ClassifiIconButton(
                    onClick = {
                        onToggleCameraUseState(cameraUseState)
                    },
                    icon = {
                        Icon(
                            painterResource(
                                id =
                                when (cameraUseState) {
                                    is CameraUseState.Photo -> {
                                        ClassifiIcons.VideoCamera
                                    }

                                    is CameraUseState.Video -> {
                                        ClassifiIcons.Snapshot
                                    }
                                }
                            ),
                            contentDescription = null
                        )
                    }
                )
            },
            libraryButton = {
                ClassifiIconButton(
                    onClick = onViewAlbum,
                    icon = {
                        Icon(
                            painterResource(id = ClassifiIcons.Album),
                            contentDescription = null
                        )
                    }
                )
            }
        )
    }
}

@Composable
fun CameraTopButtons(
    closeButton: @Composable () -> Unit,
    cameraToggleButtons: @Composable RowScope.() -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box {
            closeButton()
        }

        Box(
            Modifier.background(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
            )
        ) {
            Row { cameraToggleButtons() }
        }
    }
}


@Composable
fun CameraBottomButtons(
    snapshotButton: @Composable () -> Unit,
    videoButton: @Composable () -> Unit,
    libraryButton: @Composable () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            ), horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box { libraryButton() }
        Box { snapshotButton() }
        Box { videoButton() }
    }
}

object ClassifiCameraDefaults {
    val edgePadding = 16.dp
}