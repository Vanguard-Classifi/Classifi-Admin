package com.khalidtouch.classifiadmin.settings.navigation.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
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
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel
import com.khalidtouch.core.designsystem.components.ClassifiAvatar
import com.khalidtouch.core.designsystem.components.ClassifiImageAvatar
import com.khalidtouch.core.designsystem.components.SettingItem
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.khalidtouch.core.designsystem.icons.ClassifiImages

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenWrapper(
    settingsViewModel: SettingsViewModel,
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
        ProfileScreen(
            settingsViewModel = settingsViewModel,
            profileViewModel = profileViewModel,
        )
    }
}


@Composable
internal fun ProfileScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    profileViewModel: ProfileViewModel,
) {
    val context = LocalContext.current
    val headerStyle = MaterialTheme.typography.titleMedium.copy(
        color = Color.Black.copy(0.8f)
    )
    val textStyle = MaterialTheme.typography.bodyLarge
    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val me by settingsViewModel.observeMe.collectAsStateWithLifecycle()

    LazyColumn(Modifier.fillMaxSize()) {
        when (uiState) {
            is ProfileScreenUiState.Loading -> Unit
            is ProfileScreenUiState.Success -> {
                nameItem(
                    settingsViewModel = settingsViewModel,
                    headerStyle = headerStyle,
                    textStyle = textStyle,
                    username = me?.account?.username
                        ?: context.getString(R.string.click_to_add_username)
                )

                phoneItem(
                    settingsViewModel = settingsViewModel,
                    headerStyle = headerStyle,
                    textStyle = textStyle,
                    phone = me?.profile?.phone ?: context.getString(R.string.click_to_add_phone),
                )

                bioItem(
                    settingsViewModel = settingsViewModel,
                    headerStyle = headerStyle,
                    textStyle = textStyle,
                    bio = me?.profile?.bio ?: context.getString(R.string.click_to_add_bio)
                )

                dobItem(
                    settingsViewModel = settingsViewModel,
                    headerStyle = headerStyle,
                    textStyle = textStyle,
                    dob = me?.profile?.dob ?: context.getString(R.string.click_to_add_dob)
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
                    address = me?.profile?.contact?.address
                        ?: context.getString(R.string.click_to_add_address)
                )

                countryItem(
                    settingsViewModel = settingsViewModel,
                    headerStyle = headerStyle,
                    textStyle = textStyle,
                    country = me?.profile?.contact?.country
                        ?: context.getString(R.string.click_to_add_country)
                )

                stateItem(
                    settingsViewModel = settingsViewModel,
                    headerStyle = headerStyle,
                    textStyle = textStyle,
                    stateOfCountry = me?.profile?.contact?.stateOfCountry
                        ?: context.getString(R.string.click_to_add_state)
                )

                cityItem(
                    settingsViewModel = settingsViewModel,
                    headerStyle = headerStyle,
                    textStyle = textStyle,
                    city = me?.profile?.contact?.city ?: context.getString(R.string.click_to_add_city)
                )

                postalCodeItem(
                    settingsViewModel = settingsViewModel,
                    headerStyle = headerStyle,
                    textStyle = textStyle,
                    postalCode = me?.profile?.contact?.postalCode
                        ?: context.getString(R.string.click_to_add_postal_code)
                )

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }

            }
        }

    }

    AnimatedVisibility(
        visible = uiState is ProfileScreenUiState.Loading,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
        ) + fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            ClassifiLoadingWheel()
        }
    }
}


fun LazyListScope.nameItem(
    settingsViewModel: SettingsViewModel,
    headerStyle: TextStyle,
    textStyle: TextStyle,
    username: String,
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
                            text = username,
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
    phone: String,
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
                            text = phone,
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
    bio: String,
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
                            text = bio,
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
    dob: String,
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
                            text = dob,
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
    address: String,
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
                            text = address,
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
    country: String,
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
                            text = country,
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
    stateOfCountry: String,
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
                            text = stateOfCountry,
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
    city: String,
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
                            text = city,
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
    postalCode: String,
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
                            text = postalCode,
                        )
                    }

                }
            },
            hasEditIcon = true,
        )
    }
}