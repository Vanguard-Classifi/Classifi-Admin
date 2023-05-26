package com.khalidtouch.classifiadmin.settings.navigation.settings

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khalidtouch.classifiadmin.settings.R
import com.khalidtouch.classifiadmin.settings.navigation.account.AccountScreenWrapper
import com.khalidtouch.classifiadmin.settings.navigation.administration.AdministrationScreenWrapper
import com.khalidtouch.classifiadmin.settings.navigation.components.SettingsTextFieldBox
import com.khalidtouch.classifiadmin.settings.navigation.preferences.PreferencesScreenWrapper
import com.khalidtouch.classifiadmin.settings.navigation.profile.ProfileScreenWrapper
import com.khalidtouch.core.designsystem.components.ClassifiBackground
import com.khalidtouch.core.designsystem.components.ClassifiScrollableTabRow
import com.khalidtouch.core.designsystem.components.ClassifiSettingDefaults
import com.khalidtouch.core.designsystem.components.ClassifiSidePane
import com.khalidtouch.core.designsystem.components.ClassifiSimpleTopAppBar
import com.khalidtouch.core.designsystem.components.ClassifiTab
import com.khalidtouch.core.designsystem.components.ClassifiTabSidePane
import com.khalidtouch.core.designsystem.components.ClassifiTextButton
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import kotlinx.coroutines.launch

@Composable
fun SettingsRoute(
    windowSizeClass: WindowSizeClass,
    onBack: () -> Unit,
) {
    SettingsScreen(
        windowSizeClass = windowSizeClass,
        onBack = onBack,
    )
}


@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
internal fun SettingsScreen(
    onBack: () -> Unit = {},
    windowSizeClass: WindowSizeClass,
    settingsViewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>(),
    textFieldColors: TextFieldColors = TextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black.copy(alpha = ClassifiSettingDefaults.textFieldAlpha),
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedLabelColor = Color.Black,
        cursorColor = Color.Black,
        focusedIndicatorColor = Color.Black,
        unfocusedIndicatorColor = Color.Black,
    ),
) {
    val TAG = "SettingsScreen"
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    ClassifiBackground {
        val snackbarHostState = remember { SnackbarHostState() }
        val selectedTabIndex by settingsViewModel.selectedTabIndex.observeAsState()
        val scope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState()
        var showModalBottomSheet by rememberSaveable {
            mutableStateOf(false)
        }
        val currentSettingItemClicked by settingsViewModel.currentSettingItemClicked.observeAsState()

        LaunchedEffect(currentSettingItemClicked!!) {
            if (currentSettingItemClicked !is SettingItemClicked.None) {
                sheetState.show()
                showModalBottomSheet = true
            }
        }

        BoxWithConstraints {
            Scaffold(
                modifier = Modifier.semantics { testTagsAsResourceId = true },
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    val headerStyle = MaterialTheme.typography.titleMedium

                    ClassifiSimpleTopAppBar(
                        title = {
                            Box(Modifier.padding(start = 16.dp)) {
                                ProvideTextStyle(headerStyle) {
                                    Text(
                                        text = stringResource(id = R.string.manage_account)
                                    )
                                }
                            }
                        },
                        navIcon = {
                            Box(modifier = Modifier) {
                                IconButton(
                                    onClick = onBack,
                                    enabled = true,
                                ) {
                                    Icon(
                                        painter = painterResource(id = ClassifiIcons.Back),
                                        contentDescription = stringResource(id = R.string.go_back)
                                    )
                                }
                            }
                        }
                    )
                },
                content = { padding ->
                    BoxWithConstraints {
                        val maxWidth = maxWidth

                        Row(
                            Modifier
                                .fillMaxSize()
                                .padding(padding)
                                .consumeWindowInsets(padding)
                                .windowInsetsPadding(
                                    WindowInsets.safeDrawing.only(
                                        WindowInsetsSides.Horizontal
                                    )
                                )
                        ) {
                            if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium ||
                                windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
                            ) {
                                //side pane
                                ClassifiSidePane(
                                    modifier = Modifier,
                                    totalWidth = maxWidth,
                                    items = {
                                        val textStyle = MaterialTheme.typography.labelLarge.copy(
                                            textAlign = TextAlign.Center
                                        )

                                        settingsViewModel.tabs.forEachIndexed { index, setting ->
                                            ClassifiTabSidePane(
                                                selected = selectedTabIndex!! == index,
                                                onClick = { settingsViewModel.updateTabIndex(index) },
                                                text = {
                                                    ProvideTextStyle(value = textStyle) {
                                                        Text(stringResource(id = setting.textRes))
                                                    }
                                                },
                                                leadingIcon = {
                                                    Icon(
                                                        painter = painterResource(id = setting.iconRes),
                                                        contentDescription = stringResource(id = setting.textRes)
                                                    )
                                                }
                                            )
                                        }
                                    }
                                )
                            }

                            Column(Modifier.weight(1f)) {
                                if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                                    //settings tab
                                    ClassifiScrollableTabRow(
                                        selectedTabIndex = selectedTabIndex!!,
                                        tabs = {
                                            settingsViewModel.tabs.forEachIndexed { index, setting ->
                                                ClassifiTab(
                                                    selected = selectedTabIndex!! == index,
                                                    onClick = {
                                                        settingsViewModel.updateTabIndex(index)
                                                    },
                                                    text = {
                                                        Text(stringResource(id = setting.textRes))
                                                    },
                                                    leadingIcon = {
                                                        Icon(
                                                            painter = painterResource(id = setting.iconRes),
                                                            contentDescription = stringResource(id = setting.textRes)
                                                        )
                                                    }
                                                )
                                            }
                                        },
                                        pageSize = settingsViewModel.tabs.size
                                    )
                                }

                                //screens
                                when (selectedTabIndex!!) {
                                    /**
                                     * 0 -> Profile
                                     * 1 -> Account
                                     * 2 -> Preferences
                                     * 3 -> Administration
                                     */
                                    0 -> ProfileScreenWrapper()
                                    1 -> AccountScreenWrapper()
                                    2 -> PreferencesScreenWrapper()
                                    3 -> AdministrationScreenWrapper()
                                }
                            }
                        }
                    }
                }
            )

            if (showModalBottomSheet) {
                when (uiState) {
                    is SettingsUiState.Loading -> Unit
                    is SettingsUiState.Success -> {
                        ModalBottomSheet(
                            onDismissRequest = {
                                scope.launch {
                                    sheetState.hide()
                                    showModalBottomSheet = false
                                    settingsViewModel.cancelSettingItemClicked()
                                }
                            },
                            sheetState = sheetState,
                            containerColor = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(
                                topStartPercent = 5,
                                topEndPercent = 5
                            ),
                            tonalElevation = 2.dp,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            content = {
                                val headerStyle = MaterialTheme.typography.titleMedium

                                when (currentSettingItemClicked!!) {
                                    is SettingItemClicked.Name -> {
                                        SettingsTextFieldBox(
                                            textField = {
                                                OutlinedTextField(
                                                    value = (uiState as SettingsUiState.Success)
                                                        .data.profileData?.personalData?.username.orEmpty(),
                                                    onValueChange = settingsViewModel::onUsernameChanged,
                                                    enabled = true,
                                                    keyboardOptions = KeyboardOptions(),
                                                    colors = textFieldColors,
                                                    maxLines = 1,
                                                    singleLine = true,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            },
                                            header = {
                                                ProvideTextStyle(headerStyle) {
                                                    Text(stringResource(id = R.string.enter_your_name))
                                                }
                                            },
                                            responseButtons = {
                                                ClassifiTextButton(
                                                    onClick = { /*TODO* on Cancel */ },
                                                    text = {
                                                        Text(
                                                            stringResource(id = R.string.cancel),
                                                            modifier = Modifier.padding(
                                                                horizontal = ClassifiSettingDefaults.textPadding
                                                            )
                                                        )
                                                    }
                                                )

                                                ClassifiTextButton(
                                                    onClick = { /*TODO* on Save */ },
                                                    text = {
                                                        Text(
                                                            stringResource(id = R.string.save),
                                                            modifier = Modifier.padding(
                                                                horizontal = ClassifiSettingDefaults.textPadding
                                                            )
                                                        )
                                                    }
                                                )

                                            }
                                        )
                                    }

                                    is SettingItemClicked.Phone -> {

                                    }

                                    is SettingItemClicked.Bio -> {

                                    }

                                    is SettingItemClicked.Dob -> {

                                    }

                                    is SettingItemClicked.Address -> {

                                    }

                                    is SettingItemClicked.Country -> {

                                    }

                                    is SettingItemClicked.StateOfCountry -> {

                                    }

                                    is SettingItemClicked.City -> {

                                    }

                                    is SettingItemClicked.PostalCode -> {

                                    }

                                    else -> {

                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

    }
}


enum class Settings(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int,
    destination: String,
) {
    Profile(
        ClassifiIcons.Profile,
        R.string.profile,
        ""
    ),
    Account(
        ClassifiIcons.Settings,
        R.string.account,
        ""
    ),
    Preferences(
        ClassifiIcons.Preference,
        R.string.preferences,
        ""
    ),
    Administration(
        ClassifiIcons.Admin,
        R.string.administration,
        ""
    )
}

sealed interface SettingItemClicked {
    object None : SettingItemClicked
    object Name : SettingItemClicked
    object Phone : SettingItemClicked
    object Bio : SettingItemClicked
    object Dob : SettingItemClicked
    object Address : SettingItemClicked
    object Country : SettingItemClicked
    object StateOfCountry : SettingItemClicked
    object City : SettingItemClicked
    object PostalCode : SettingItemClicked
}