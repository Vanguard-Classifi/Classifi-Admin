package com.khalidtouch.core.designsystem.ui

import android.net.Uri
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.khalidtouch.core.designsystem.theme.ContentAlpha
import com.khalidtouch.core.designsystem.theme.LocalContentAlpha

@Composable
fun ClassifiUserThumbnail(
    username: String,
    email: String,
    profileImage: String,
    profileImageIsDefault: Boolean,
    bio: String,
    selected: Boolean,
    onClick: () -> Unit = {},
    onLongPress: () -> Unit = {},
) {
    var tapped by remember { mutableStateOf(false) }
    var held by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        Modifier
            .indication(interactionSource, LocalIndication.current)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset ->
                        held = true
                        onLongPress()
                        val holdState = PressInteraction.Press(offset)
                        interactionSource.tryEmit(holdState)
                        interactionSource.tryEmit(PressInteraction.Release(holdState))
                        held = false
                    },
                    onTap = { offset ->
                        tapped = true
                        onClick()
                        val tapState = PressInteraction.Press(offset)
                        interactionSource.tryEmit(tapState)
                        interactionSource.tryEmit(PressInteraction.Release(tapState))
                        tapped = false
                    }
                )
            }
            .background(
                if(selected) {
                    MaterialTheme.colorScheme.outline.copy(0.1f)
                } else Color.Transparent
            )
        , contentAlignment = Alignment.CenterStart
    ) {

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha(0.7f)) {
            val imagePadding = remember { mutableStateOf<Int>(0) }
            Box(
                Modifier
                    .padding(8.dp)
                    .onGloballyPositioned {
                        imagePadding.value = it.size.width
                    },
                contentAlignment = Alignment.CenterStart
            ) {

                when {
                    profileImageIsDefault -> {
                        Icon(
                            painterResource(id = ClassifiIcons.Personal),
                            contentDescription = null,
                            modifier = Modifier
                                .height(48.dp)
                                .width(48.dp),
                            tint = MaterialTheme.colorScheme.outline.copy(LocalContentAlpha.current.alpha)
                        )
                    }

                    else -> {
                        val profileImageUri = Uri.parse(profileImage)
                        AsyncImage(
                            modifier = Modifier
                                .height(48.dp)
                                .width(48.dp)
                                .clip(CircleShape),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(profileImageUri).build(),
                            contentDescription = null,
                            alpha = LocalContentAlpha.current.alpha,
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp,
                        end = 16.dp,
                        start = 16.dp + with(LocalDensity.current) {
                            imagePadding.value.toDp()
                        }
                    ),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = username,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.outline.copy(LocalContentAlpha.current.alpha),
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = email,
                    style = MaterialTheme.typography.titleSmall.copy(
                        MaterialTheme.colorScheme.outline.copy(LocalContentAlpha.current.alpha),
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = bio,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        MaterialTheme.colorScheme.outline.copy(LocalContentAlpha.current.alpha),
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Composable
@Preview
private fun UserThumbnailPreview() {
    ClassifiUserThumbnail(
        username = "Khalid Isah",
        bio = "This is a simple bio about myself, yeah",
        selected = false,
        profileImage = "",
        email = "",
        profileImageIsDefault = false,
    )
}