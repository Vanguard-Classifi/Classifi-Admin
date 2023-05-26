package com.khalidtouch.classifiadmin.feeds.mediastore

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
fun GalleryNoPermissionScreen(
    onRequestReadExternalStoragePermission: () -> Unit,
    onRequestWriteExternalStoragePermission: () -> Unit,
    writeExternalStoragePermissionState: PermissionState,
    readExternalStoragePermissionState: PermissionState,
    onDismissDialog: () -> Unit,
) {
    val configuration = LocalConfiguration.current

    Box {
        if(!writeExternalStoragePermissionState.status.isGranted) {
            AlertDialog(
                properties = DialogProperties(usePlatformDefaultWidth = false),
                modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
                onDismissRequest = {
                    onDismissDialog()
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.write_external_storage),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.proceed_to_write_external_storage),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                confirmButton = {
                    ClassifiTextButton(
                        onClick = { onRequestWriteExternalStoragePermission() },
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

        if(!readExternalStoragePermissionState.status.isGranted) {
            Box {
                AlertDialog(
                    properties = DialogProperties(usePlatformDefaultWidth = false),
                    modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
                    onDismissRequest = {
                        onDismissDialog()
                    },
                    title = {
                        Text(
                            text = stringResource(id = R.string.read_external_storage),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(id = R.string.proceed_to_read_external_storage),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    confirmButton = {
                        ClassifiTextButton(
                            onClick = { onRequestReadExternalStoragePermission() },
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