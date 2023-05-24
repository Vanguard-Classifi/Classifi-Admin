package com.khalidtouch.classifiadmin.feeds.takephoto

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.khalidtouch.classifiadmin.feeds.R
import com.khalidtouch.core.designsystem.components.ClassifiButton
import com.khalidtouch.core.designsystem.components.ClassifiTextButton

@Composable
fun PhotoCaptureScreen(
    uiState: TakePhotoUiState,
    onNext: () -> Unit,
    onCancel: () -> Unit,
) {
    when(uiState) {
        is TakePhotoUiState.Loading -> Unit
        is TakePhotoUiState.Loaded -> {
            AnimatedVisibility(
                visible = !uiState.data.hasNoSavedMedia,
                enter = slideInHorizontally(
                    initialOffsetX = { fullHeight -> -fullHeight },
                ) + fadeIn(),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullHeight -> -fullHeight },
                ) + fadeOut(),
            ) {
                BoxWithConstraints {

                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(uiState.data.mediaUri).build(),
                        contentDescription = stringResource(id = R.string.saved_image)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = ClassifiCameraDefaults.edgePadding.times(3f),
                                start = ClassifiCameraDefaults.edgePadding,
                                end = ClassifiCameraDefaults.edgePadding,
                            )
                    ) {
                        PhotoCaptureTopButtons(
                            closeButton = {
                                ClassifiButton(
                                    onClick = onCancel,
                                    enabled = true,
                                    text = {
                                        Text(
                                            text = stringResource(id = R.string.cancel),
                                            style = MaterialTheme.typography.labelLarge,
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = MaterialTheme.colorScheme.background,
                                    )
                                )
                            },
                            nextButton = {
                                ClassifiButton(
                                    onClick = onNext,
                                    enabled = true,
                                    text = {
                                        Text(
                                            text = stringResource(id = R.string.next),
                                            style = MaterialTheme.typography.labelLarge,
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.background,
                                        contentColor = MaterialTheme.colorScheme.onBackground,
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PhotoCaptureTopButtons(
    closeButton: @Composable () -> Unit,
    nextButton: @Composable () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box {
            closeButton()
        }

        Box {
            nextButton()
        }
    }
}
