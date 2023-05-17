package com.khalidtouch.classifiadmin.settings.navigation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.khalidtouch.classifiadmin.settings.R
import com.khalidtouch.classifiadmin.settings.navigation.settings.SettingsViewModel
import com.khalidtouch.core.designsystem.components.ClassifiAvatar
import com.khalidtouch.core.designsystem.components.ClassifiImageAvatar
import com.khalidtouch.core.designsystem.components.SettingItem
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.khalidtouch.core.designsystem.icons.ClassifiImages

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenWrapper(
    settingsViewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>()
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .draggable(
                state = settingsViewModel.dragState.value!!,
                orientation = Orientation.Horizontal,
                onDragStarted = {},
                onDragStopped = {
                    settingsViewModel.updateTabIndexBasedOnSwipe()
                }
            )
    ) {
        ProfileScreen()
    }
}


@Composable
internal fun ProfileScreen(
    modifier: Modifier = Modifier,
    iconButtonColors: IconButtonColors = IconButtonDefaults.iconButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ),
) {
    LazyColumn(Modifier.fillMaxSize()) {
        item {

            val headerStyle = MaterialTheme.typography.titleSmall.copy(
                color = Color.Black.copy(0.8f)
            )
            val textStyle = MaterialTheme.typography.bodyLarge

            /**
             * The user profile image
             */

            /**
             * Name
             */
            SettingItem(
                modifier = modifier,
                onClick = {},
                icon = {
                    Icon(
                        painter = painterResource(id = ClassifiIcons.Personal),
                        contentDescription = null
                    )
                },
                text = {
                    Column {
                        ProvideTextStyle(value = headerStyle) {
                            Text(
                                text = "Name"
                            )
                        }

                        ProvideTextStyle(value = textStyle) {
                            Text(
                                text = "khalidtouch"
                            )
                        }

                    }
                },
                hasEditIcon = true,
            )

            /**
             * Phone
             */
            SettingItem(
                modifier = modifier,
                onClick = {},
                icon = {
                    Icon(
                        painter = painterResource(id = ClassifiIcons.Phone),
                        contentDescription = null
                    )
                },
                text = {
                    Column {
                        ProvideTextStyle(value = headerStyle) {
                            Text(
                                text = "Phone"
                            )
                        }

                        ProvideTextStyle(value = textStyle) {
                            Text(
                                text = "08163429553"
                            )
                        }

                    }
                },
                hasEditIcon = true,
            )

            /**
             * Your bio
             */
            SettingItem(
                modifier = modifier,
                onClick = {},
                icon = {
                    Icon(
                        painter = painterResource(id = ClassifiIcons.About),
                        contentDescription = null
                    )
                },
                text = {
                    Column {
                        ProvideTextStyle(value = headerStyle) {
                            Text(
                                text = "Bio"
                            )
                        }

                        ProvideTextStyle(value = textStyle) {
                            Text(
                                text = "I'm a dedicated student of knowledge, I love to learn and I'm curious to understand how things work",
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                    }
                },
                hasEditIcon = true,
            )

            /**
             * Date of birth
             */
            SettingItem(
                modifier = modifier,
                onClick = {},
                icon = {
                    Icon(
                        painter = painterResource(id = ClassifiIcons.Dob),
                        contentDescription = null
                    )
                },
                text = {
                    Column {
                        ProvideTextStyle(value = headerStyle) {
                            Text(
                                text = "Date of birth"
                            )
                        }

                        ProvideTextStyle(value = textStyle) {
                            Text(
                                text = "1993-02-21",
                            )
                        }

                    }
                },
                hasEditIcon = true,
            )

            Divider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

        }
    }
}