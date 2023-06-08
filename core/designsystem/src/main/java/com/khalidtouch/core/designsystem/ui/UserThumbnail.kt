package com.khalidtouch.core.designsystem.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
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
    Box(
        Modifier
            .clickable(
                enabled = true,
                onClick = onClick,
                role = Role.Button,
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongPress()
                    }
                )
            }, contentAlignment = Alignment.CenterStart
    ) {
      CompositionLocalProvider(LocalContentAlpha provides ContentAlpha(0.7f)) {
          val imagePadding = remember { mutableStateOf<Int>(0) }
          Box(
              Modifier
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