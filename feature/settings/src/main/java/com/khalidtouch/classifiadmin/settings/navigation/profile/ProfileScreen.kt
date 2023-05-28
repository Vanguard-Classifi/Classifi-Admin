package com.khalidtouch.classifiadmin.settings.navigation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khalidtouch.classifiadmin.settings.R
import com.khalidtouch.classifiadmin.settings.navigation.settings.SettingItemClicked
import com.khalidtouch.classifiadmin.settings.navigation.settings.SettingsViewModel
import com.khalidtouch.core.designsystem.components.ClassifiAvatar
import com.khalidtouch.core.designsystem.components.ClassifiImageAvatar
import com.khalidtouch.core.designsystem.components.SettingItem
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.khalidtouch.core.designsystem.icons.ClassifiImages

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenWrapper(
    settingsViewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>(),
    profileViewModel: ProfileViewModel = hiltViewModel<ProfileViewModel>(),
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
    settingsViewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>(),
    profileViewModel: ProfileViewModel = hiltViewModel<ProfileViewModel>(),
) {
    val headerStyle = MaterialTheme.typography.titleSmall.copy(
        color = Color.Black.copy(0.8f)
    )
    val textStyle = MaterialTheme.typography.bodyMedium

    LazyColumn(Modifier.fillMaxSize()) {
        nameItem(
            settingsViewModel = settingsViewModel,
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        phoneItem(
            settingsViewModel = settingsViewModel,
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        bioItem(
            settingsViewModel = settingsViewModel,
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        dobItem(
            settingsViewModel = settingsViewModel,
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        item {
            Spacer(modifier = modifier.height(8.dp))
            Divider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = modifier.height(8.dp))
        }

        addressItem(
            settingsViewModel = settingsViewModel,
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        countryItem(
            settingsViewModel = settingsViewModel,
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        stateItem(
            settingsViewModel = settingsViewModel,
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        cityItem(
            settingsViewModel = settingsViewModel,
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        postalCodeItem(
            settingsViewModel = settingsViewModel,
            headerStyle = headerStyle,
            textStyle = textStyle,
        )

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }

    }
}


fun LazyListScope.nameItem(
    settingsViewModel: SettingsViewModel,
    headerStyle: TextStyle,
    textStyle: TextStyle,
) {
    item {
        /**
         * Name
         */
        Spacer(modifier = Modifier.height(32.dp))

        SettingItem(
            onClick = {
                settingsViewModel.updateCurrentSettingItemClicked(
                    SettingItemClicked.Name
                )
            },
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
                            text = stringResource(id = R.string.name)
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
    }
}


fun LazyListScope.phoneItem(
    settingsViewModel: SettingsViewModel,
    headerStyle: TextStyle,
    textStyle: TextStyle,
) {
    item {
        /**
         * Phone
         */
        SettingItem(
            onClick = {
                settingsViewModel.updateCurrentSettingItemClicked(
                    SettingItemClicked.Phone
                )
            },
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
                            text = stringResource(R.string.phone)
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
    }
}

fun LazyListScope.bioItem(
    settingsViewModel: SettingsViewModel,
    headerStyle: TextStyle,
    textStyle: TextStyle,
) {
    item {
        /**
         * Your bio
         */
        SettingItem(
            onClick = {
                settingsViewModel.updateCurrentSettingItemClicked(
                    SettingItemClicked.Bio
                )
            },
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
                            text = stringResource(id = R.string.bio)
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
    }
}

fun LazyListScope.dobItem(
    settingsViewModel: SettingsViewModel,
    headerStyle: TextStyle,
    textStyle: TextStyle,
) {
    item {
        /**
         * Date of birth
         */
        SettingItem(
            onClick = {
                settingsViewModel.updateCurrentSettingItemClicked(
                    SettingItemClicked.Dob
                )
            },
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
                            text = stringResource(id = R.string.dob)
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
    }
}


fun LazyListScope.addressItem(
    settingsViewModel: SettingsViewModel,
    headerStyle: TextStyle,
    textStyle: TextStyle,
) {
    item {
        /**
         * Street/Address
         */
        SettingItem(
            onClick = {
                settingsViewModel.updateCurrentSettingItemClicked(
                    SettingItemClicked.Address
                )
            },
            icon = {
                Icon(
                    painter = painterResource(id = ClassifiIcons.Address),
                    contentDescription = null
                )
            },
            text = {
                Column {
                    ProvideTextStyle(value = headerStyle) {
                        Text(
                            text = stringResource(id = R.string.address)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "No 23. Poly road",
                        )
                    }

                }
            },
            hasEditIcon = true,
        )
    }
}

fun LazyListScope.countryItem(
    settingsViewModel: SettingsViewModel,
    headerStyle: TextStyle,
    textStyle: TextStyle,
) {
    item {
        /**
         * Country
         */
        SettingItem(
            onClick = {
                settingsViewModel.updateCurrentSettingItemClicked(
                    SettingItemClicked.Country
                )
            },
            icon = {
                Icon(
                    painter = painterResource(id = ClassifiIcons.Country),
                    contentDescription = null
                )
            },
            text = {
                Column {
                    ProvideTextStyle(value = headerStyle) {
                        Text(
                            text = stringResource(id = R.string.country)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "Nigeria",
                        )
                    }

                }
            },
            hasEditIcon = true,
        )
    }
}

fun LazyListScope.stateItem(
    settingsViewModel: SettingsViewModel,
    headerStyle: TextStyle,
    textStyle: TextStyle,
) {
    item {
        /**
         * State in Country
         */
        SettingItem(
            onClick = {
                settingsViewModel.updateCurrentSettingItemClicked(
                    SettingItemClicked.StateOfCountry
                )
            },
            icon = {
                Icon(
                    painter = painterResource(id = ClassifiIcons.Country),
                    contentDescription = null
                )
            },
            text = {
                Column {
                    ProvideTextStyle(value = headerStyle) {
                        Text(
                            text = stringResource(id = R.string.state)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "Edo",
                        )
                    }

                }
            },
            hasEditIcon = true,
        )
    }
}

fun LazyListScope.cityItem(
    settingsViewModel: SettingsViewModel,
    headerStyle: TextStyle,
    textStyle: TextStyle,
) {
    item {
        /**
         * City
         */
        SettingItem(
            onClick = {
                settingsViewModel.updateCurrentSettingItemClicked(
                    SettingItemClicked.City
                )
            },
            icon = {
                Icon(
                    painter = painterResource(id = ClassifiIcons.City),
                    contentDescription = null
                )
            },
            text = {
                Column {
                    ProvideTextStyle(value = headerStyle) {
                        Text(
                            text = stringResource(id = R.string.city)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "Auchi",
                        )
                    }

                }
            },
            hasEditIcon = true,
        )
    }
}

fun LazyListScope.postalCodeItem(
    settingsViewModel: SettingsViewModel,
    headerStyle: TextStyle,
    textStyle: TextStyle,
) {
    item {
        /**
         * Postal code of city
         */
        SettingItem(
            onClick = {
                settingsViewModel.updateCurrentSettingItemClicked(
                    SettingItemClicked.PostalCode
                )
            },
            icon = {
                Icon(
                    painter = painterResource(id = ClassifiIcons.Postal),
                    contentDescription = null
                )
            },
            text = {
                Column {
                    ProvideTextStyle(value = headerStyle) {
                        Text(
                            text = stringResource(id = R.string.postal_code)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "234587",
                        )
                    }

                }
            },
            hasEditIcon = true,
        )
    }
}