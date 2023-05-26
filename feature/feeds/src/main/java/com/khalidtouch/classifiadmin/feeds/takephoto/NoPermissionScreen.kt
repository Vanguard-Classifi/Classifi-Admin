package com.khalidtouch.classifiadmin.feeds.takephoto

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.khalidtouch.classifiadmin.feeds.R
import com.khalidtouch.core.designsystem.components.ClassifiTextButton


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NoPermissionScreen(
    onRequestCameraPermission: () -> Unit,
    onRequestAudioPermission: () -> Unit,
    cameraPermissionState: PermissionState,
    audioPermissionState: PermissionState,
    onDismissDialog: () -> Unit,
) {
    val configuration = LocalConfiguration.current


    Box {
        if(!audioPermissionState.status.isGranted) {
            AlertDialog(
                properties = DialogProperties(usePlatformDefaultWidth = false),
                modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
                onDismissRequest = {
                    onDismissDialog()
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.audio_permission),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.proceed_to_use_audio),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                confirmButton = {
                    ClassifiTextButton(
                        onClick = { onRequestAudioPermission() },
                        enabled = true,
                        content = {
                            Text(
                                text = stringResource(id = R.string.proceed),
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                    )
                },
                dismissButton = {
                    ClassifiTextButton(
                        onClick = { onDismissDialog() },
                        enabled = true,
                        content = {
                            Text(
                                text = stringResource(id = R.string.cancel),
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                    )
                },
                tonalElevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
            )
        }

        if(!cameraPermissionState.status.isGranted) {
            Box {
                AlertDialog(
                    properties = DialogProperties(usePlatformDefaultWidth = false),
                    modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
                    onDismissRequest = {
                        onDismissDialog()
                    },
                    title = {
                        Text(
                            text = stringResource(id = R.string.camera_permission),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(id = R.string.proceed_to_use_camera),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    confirmButton = {
                        ClassifiTextButton(
                            onClick = { onRequestCameraPermission() },
                            enabled = true,
                            content = {
                                Text(
                                    text = stringResource(id = R.string.proceed),
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                        )
                    },
                    dismissButton = {
                        ClassifiTextButton(
                            onClick = { onDismissDialog() },
                            enabled = true,
                            content = {
                                Text(
                                    text = stringResource(id = R.string.cancel),
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                        )
                    },
                    tonalElevation = 2.dp,
                    shape = RoundedCornerShape(16.dp),
                )
            }
        }
    }

}
