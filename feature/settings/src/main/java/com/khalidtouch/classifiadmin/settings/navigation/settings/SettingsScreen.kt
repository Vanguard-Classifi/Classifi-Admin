package com.khalidtouch.classifiadmin.settings.navigation.settings

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.khalidtouch.classifiadmin.model.Country
import com.khalidtouch.classifiadmin.model.DarkThemeConfig
import com.khalidtouch.classifiadmin.settings.R
import com.khalidtouch.classifiadmin.settings.navigation.account.AccountScreenWrapper
import com.khalidtouch.classifiadmin.settings.navigation.administration.AdministrationScreenWrapper
import com.khalidtouch.classifiadmin.settings.navigation.components.SettingsTextFieldBox
import com.khalidtouch.classifiadmin.settings.navigation.preferences.PreferencesScreenWrapper
import com.khalidtouch.classifiadmin.settings.navigation.profile.ProfileScreenWrapper
import com.khalidtouch.core.common.extensions.asDateString
import com.khalidtouch.core.common.extensions.asLocalDate
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel
import com.khalidtouch.core.designsystem.components.ClassifiBackground
import com.khalidtouch.core.designsystem.components.ClassifiScrollableTabRow
import com.khalidtouch.core.designsystem.components.ClassifiSettingDefaults
import com.khalidtouch.core.designsystem.components.ClassifiSidePane
import com.khalidtouch.core.designsystem.components.ClassifiSimpleTopAppBar
import com.khalidtouch.core.designsystem.components.ClassifiTab
import com.khalidtouch.core.designsystem.components.ClassifiTabSidePane
import com.khalidtouch.core.designsystem.components.ClassifiTextButton
import com.khalidtouch.core.designsystem.extensions.selectableGroup
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.khalidtouch.core.designsystem.ui.ClassifiDatePicker
import com.khalidtouch.core.designsystem.ui.ClassifiDatePickerDialog
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun SettingsRoute(
    windowSizeClass: WindowSizeClass,
    onBack: () -> Unit,
    onOpenSchoolAdminPanel: () -> Unit,
    onOpenTeacherAdminPanel: () -> Unit,
) {
    SettingsScreen(
        windowSizeClass = windowSizeClass,
        onBack = onBack,
        onOpenSchoolAdminPanel = onOpenSchoolAdminPanel,
        onOpenTeacherAdminPanel = onOpenTeacherAdminPanel,
    )
}


@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
internal fun SettingsScreen(
    onBack: () -> Unit = {},
    onOpenSchoolAdminPanel: () -> Unit,
    onOpenTeacherAdminPanel: () -> Unit,
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
        val context = LocalContext.current
        val configuration = LocalConfiguration.current
        val snackbarHostState = remember { SnackbarHostState() }
        val selectedTabIndex by settingsViewModel.selectedTabIndex.collectAsStateWithLifecycle()
        val scope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState()
        var showModalBottomSheet by rememberSaveable {
            mutableStateOf(false)
        }
        val currentSettingItemClicked by settingsViewModel.currentSettingItemClicked.observeAsState()
        val countries = settingsViewModel.countryPagingSource.collectAsLazyPagingItems()
        val datePickerState = rememberDatePickerState(
            initialDisplayMode = DisplayMode.Input,
            selectableDates = object : SelectableDates {
                override fun isSelectableYear(year: Int): Boolean {
                    return LocalDate.now().year > year
                }
            }
        )

        Log.e(TAG, "SettingsScreen: selected tab index is currently $selectedTabIndex")

        var pickerDialogState by rememberSaveable { mutableStateOf(false) }
        val darkThemeConfigDialogState by settingsViewModel.darkThemeConfigDialog.collectAsStateWithLifecycle()
        val me by settingsViewModel.observeMe.collectAsStateWithLifecycle()

        LaunchedEffect(currentSettingItemClicked!!, pickerDialogState) {
            if (
                currentSettingItemClicked !is SettingItemClicked.None &&
                currentSettingItemClicked !is SettingItemClicked.Dob
            ) {
                sheetState.show()
                showModalBottomSheet = true
                pickerDialogState = false
            }

            if (currentSettingItemClicked is SettingItemClicked.Dob) {
                pickerDialogState = true
            }
        }

        LaunchedEffect(uiState) {
            when (uiState) {
                is SettingsUiState.Loading -> Unit
                is SettingsUiState.Success -> {
                    if ((uiState as SettingsUiState.Success).data.hasUserProfileUpdated) {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.profile_updated_successfully),
                        )
                    }
                }
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
                                when (selectedTabIndex) {
                                    /**
                                     * 0 -> Profile
                                     * 1 -> Account
                                     * 2 -> Preferences
                                     * 3 -> Administration
                                     */
                                    0 -> ProfileScreenWrapper(settingsViewModel = settingsViewModel)
                                    1 -> AccountScreenWrapper(settingsViewModel = settingsViewModel)
                                    2 -> PreferencesScreenWrapper(settingsViewModel = settingsViewModel)
                                    3 -> AdministrationScreenWrapper(
                                        onOpenSchoolAdminPanel = onOpenSchoolAdminPanel,
                                        onOpenTeacherAdminPanel = onOpenTeacherAdminPanel,
                                        settingsViewModel = settingsViewModel,
                                    )
                                }
                            }
                        }
                    }
                }
            )

            //bottom sheet
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
                                                    onClick = {
                                                        settingsViewModel.resetUsername()
                                                        scope.launch {
                                                            sheetState.hide()
                                                            showModalBottomSheet = false
                                                            settingsViewModel.cancelSettingItemClicked()
                                                        }
                                                    },
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
                                                    onClick = {
                                                        //save user name to db
                                                        settingsViewModel.updateUsernameToDb(me)
                                                        scope.launch {
                                                            sheetState.hide()
                                                            showModalBottomSheet = false
                                                            settingsViewModel.cancelSettingItemClicked()
                                                        }
                                                    },
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
                                        SettingsTextFieldBox(
                                            textField = {
                                                OutlinedTextField(
                                                    value = (uiState as SettingsUiState.Success)
                                                        .data.profileData?.personalData?.phone.orEmpty(),
                                                    onValueChange = settingsViewModel::onPhoneChanged,
                                                    enabled = true,
                                                    keyboardOptions = KeyboardOptions(
                                                        keyboardType = KeyboardType.Phone
                                                    ),
                                                    colors = textFieldColors,
                                                    maxLines = 1,
                                                    singleLine = true,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            },
                                            header = {
                                                ProvideTextStyle(headerStyle) {
                                                    Text(stringResource(id = R.string.phone))
                                                }
                                            },
                                            responseButtons = {
                                                ClassifiTextButton(
                                                    onClick = {
                                                        settingsViewModel.resetPhone()
                                                        scope.launch {
                                                            sheetState.hide()
                                                            showModalBottomSheet = false
                                                            settingsViewModel.cancelSettingItemClicked()
                                                        }
                                                    },
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
                                                    onClick = {
                                                        settingsViewModel.updatePhoneToDb(me)
                                                        scope.launch {
                                                            sheetState.hide()
                                                            showModalBottomSheet = false
                                                            settingsViewModel.cancelSettingItemClicked()
                                                        }
                                                    },
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

                                    is SettingItemClicked.Bio -> {
                                        SettingsTextFieldBox(
                                            textField = {
                                                OutlinedTextField(
                                                    value = (uiState as SettingsUiState.Success)
                                                        .data.profileData?.personalData?.bio.orEmpty(),
                                                    onValueChange = settingsViewModel::onBioChanged,
                                                    enabled = true,
                                                    keyboardOptions = KeyboardOptions(),
                                                    colors = textFieldColors,
                                                    singleLine = false,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            },
                                            header = {
                                                ProvideTextStyle(headerStyle) {
                                                    Text(stringResource(id = R.string.enter_your_bio))
                                                }
                                            },
                                            responseButtons = {
                                                ClassifiTextButton(
                                                    onClick = {
                                                        settingsViewModel.resetBio()
                                                        scope.launch {
                                                            sheetState.hide()
                                                            showModalBottomSheet = false
                                                            settingsViewModel.cancelSettingItemClicked()
                                                        }
                                                    },
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
                                                    onClick = {
                                                        settingsViewModel.updateBioToDb(me)
                                                        scope.launch {
                                                            sheetState.hide()
                                                            showModalBottomSheet = false
                                                            settingsViewModel.cancelSettingItemClicked()
                                                        }
                                                    },
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

                                    is SettingItemClicked.Address -> {
                                        SettingsTextFieldBox(
                                            textField = {
                                                OutlinedTextField(
                                                    value = (uiState as SettingsUiState.Success)
                                                        .data.profileData?.locationData?.address.orEmpty(),
                                                    onValueChange = settingsViewModel::onAddressChanged,
                                                    enabled = true,
                                                    keyboardOptions = KeyboardOptions(),
                                                    colors = textFieldColors,
                                                    singleLine = false,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            },
                                            header = {
                                                ProvideTextStyle(headerStyle) {
                                                    Text(stringResource(id = R.string.enter_your_address))
                                                }
                                            },
                                            responseButtons = {
                                                ClassifiTextButton(
                                                    onClick = {
                                                        settingsViewModel.resetAddress()
                                                        scope.launch {
                                                            sheetState.hide()
                                                            showModalBottomSheet = false
                                                            settingsViewModel.cancelSettingItemClicked()
                                                        }
                                                    },
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
                                                    onClick = {
                                                        settingsViewModel.updateAddressToDb(me)
                                                        scope.launch {
                                                            sheetState.hide()
                                                            showModalBottomSheet = false
                                                            settingsViewModel.cancelSettingItemClicked()
                                                        }
                                                    },
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

                                    is SettingItemClicked.Country -> {
                                        PagedCountries(
                                            countries = countries,
                                            onClick = {
                                                settingsViewModel.onCountryChanged(it.name ?: context.getString(R.string.click_to_add_country))
                                                settingsViewModel.updateCountryToDb(me)
                                                scope.launch {
                                                    sheetState.hide()
                                                    showModalBottomSheet = false
                                                    settingsViewModel.cancelSettingItemClicked()
                                                }
                                            },
                                        )
                                    }

                                    is SettingItemClicked.StateOfCountry -> {
                                        SettingsTextFieldBox(
                                            textField = {
                                                OutlinedTextField(
                                                    value = (uiState as SettingsUiState.Success)
                                                        .data.profileData?.locationData?.stateOfCountry.orEmpty(),
                                                    onValueChange = settingsViewModel::onStateOfCountryChanged,
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
                                                    Text(stringResource(id = R.string.enter_your_state))
                                                }
                                            },
                                            responseButtons = {
                                                ClassifiTextButton(
                                                    onClick = {
                                                        settingsViewModel.resetStateOfCountry()
                                                        scope.launch {
                                                            sheetState.hide()
                                                            showModalBottomSheet = false
                                                            settingsViewModel.cancelSettingItemClicked()
                                                        }

                                                    },
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
                                                    onClick = {
                                                        settingsViewModel.updateStateOfCountryToDb(me)
                                                        scope.launch {
                                                            sheetState.hide()
                                                            showModalBottomSheet = false
                                                            settingsViewModel.cancelSettingItemClicked()
                                                        }
                                                    },
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

                                    is SettingItemClicked.City -> {
                                        SettingsTextFieldBox(
                                            textField = {
                                                OutlinedTextField(
                                                    value = (uiState as SettingsUiState.Success)
                                                        .data.profileData?.locationData?.city.orEmpty(),
                                                    onValueChange = settingsViewModel::onCityChanged,
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
                                                    Text(stringResource(id = R.string.enter_your_city))
                                                }
                                            },
                                            responseButtons = {
                                                ClassifiTextButton(
                                                    onClick = {
                                                        settingsViewModel.resetCity()
                                                        scope.launch {
                                                            sheetState.hide()
                                                            showModalBottomSheet = false
                                                            settingsViewModel.cancelSettingItemClicked()
                                                        }
                                                    },
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
                                                    onClick = {
                                                        settingsViewModel.updateCityToDb(me)
                                                        scope.launch {
                                                            sheetState.hide()
                                                            showModalBottomSheet = false
                                                            settingsViewModel.cancelSettingItemClicked()
                                                        }
                                                    },
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

                                    is SettingItemClicked.PostalCode -> {
                                        SettingsTextFieldBox(
                                            textField = {
                                                OutlinedTextField(
                                                    value = (uiState as SettingsUiState.Success)
                                                        .data.profileData?.locationData?.postalCode.orEmpty(),
                                                    onValueChange = settingsViewModel::onPostalCodeChanged,
                                                    enabled = true,
                                                    keyboardOptions = KeyboardOptions(
                                                        keyboardType = KeyboardType.Number
                                                    ),
                                                    colors = textFieldColors,
                                                    maxLines = 1,
                                                    singleLine = true,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            },
                                            header = {
                                                ProvideTextStyle(headerStyle) {
                                                    Text(stringResource(id = R.string.enter_postal_code))
                                                }
                                            },
                                            responseButtons = {
                                                ClassifiTextButton(
                                                    onClick = {
                                                        settingsViewModel.resetPostalCode()
                                                        scope.launch {
                                                            sheetState.hide()
                                                            showModalBottomSheet = false
                                                            settingsViewModel.cancelSettingItemClicked()
                                                        }
                                                    },
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
                                                    onClick = {
                                                        settingsViewModel.updatePostalCodeToDb(me)
                                                        scope.launch {
                                                            sheetState.hide()
                                                            showModalBottomSheet = false
                                                            settingsViewModel.cancelSettingItemClicked()
                                                        }
                                                    },
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

                                    else -> Unit

                                }
                            }
                        )
                    }
                }
            }

            //date picker
            if (pickerDialogState) {
                ClassifiDatePickerDialog(
                    onDismiss = {
                        pickerDialogState = false
                        settingsViewModel.cancelSettingItemClicked()
                    },
                    confirmButton = {
                        ClassifiTextButton(onClick = {
                            val date = datePickerState.selectedDateMillis
                                .asLocalDate().asDateString()
                           settingsViewModel.onDobChanged(date)
                            settingsViewModel.updateDobToDb(me)
                            pickerDialogState = false
                            settingsViewModel.cancelSettingItemClicked()
                        },
                            text = {
                                Box(Modifier.padding(horizontal = 16.dp)) {
                                    Text(
                                        text = stringResource(id = R.string.save),
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                }
                            }
                        )
                    },
                    dismissButton = {
                        ClassifiTextButton(onClick = {
                            pickerDialogState = false
                            settingsViewModel.cancelSettingItemClicked()
                        },
                            text = {
                                Box(Modifier.padding(horizontal = 16.dp)) {
                                    Text(
                                        text = stringResource(id = R.string.cancel),
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                }
                            }
                        )
                    }) {
                    ClassifiDatePicker(
                        state = datePickerState,
                    )
                }
            }

            if (darkThemeConfigDialogState) {
                AlertDialog(
                    onDismissRequest = {
                        settingsViewModel.onDarkThemeConfigDialogStateChange(false)
                    },
                    confirmButton = {
                        ClassifiTextButton(
                            onClick = {
                                /*todo on save */
                                settingsViewModel.onDarkThemeConfigDialogStateChange(false)
                            },
                            text = {
                                Text(
                                    text = stringResource(id = R.string.save),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        )
                    },
                    dismissButton = {
                        ClassifiTextButton(
                            onClick = {
                                settingsViewModel.onDarkThemeConfigDialogStateChange(false)
                            },
                            text = {
                                Text(
                                    text = stringResource(id = R.string.cancel),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        )
                    },
                    title = {
                        Text(
                            text = stringResource(id = R.string.dark_mode_settings),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    properties = DialogProperties(usePlatformDefaultWidth = false),
                    modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 16.dp),
                    tonalElevation = 2.dp,
                    text = {
                        Divider()
                        Column(Modifier.verticalScroll(rememberScrollState())) {
                            when (uiState) {
                                is SettingsUiState.Loading -> {
                                    AnimatedVisibility(
                                        visible = uiState is SettingsUiState.Loading,
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

                                is SettingsUiState.Success -> {
                                    DarkModeConfigSettingsPane(
                                        onDarkThemeConfigChanged = settingsViewModel::onDarkThemeChanged,
                                        settings = (uiState as SettingsUiState.Success).data.darkThemeSettings
                                    )
                                }
                            }
                        }
                    }
                )
            }

        }

    }
}

@Composable
fun DarkModeConfigSettingsPane(
    settings: DarkThemeConfigSettings,
    onDarkThemeConfigChanged: (DarkThemeConfig) -> Unit,
) {
    Column(Modifier.selectableGroup()) {
        DarkModeConfigRowChoose(
            onClick = { onDarkThemeConfigChanged(DarkThemeConfig.FOLLOW_SYSTEM) },
            selected = settings.darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM,
            text = stringResource(id = R.string.dark_mode_config_follow_system)
        )
        DarkModeConfigRowChoose(
            onClick = { onDarkThemeConfigChanged(DarkThemeConfig.DARK) },
            selected = settings.darkThemeConfig == DarkThemeConfig.DARK,
            text = stringResource(id = R.string.dark_mode_config_dark)
        )
        DarkModeConfigRowChoose(
            onClick = { onDarkThemeConfigChanged(DarkThemeConfig.LIGHT) },
            selected = settings.darkThemeConfig == DarkThemeConfig.LIGHT,
            text = stringResource(id = R.string.dark_mode_config_light)
        )
    }
}

@Composable
fun DarkModeConfigRowChoose(
    onClick: () -> Unit,
    selected: Boolean,
    text: String,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                role = Role.RadioButton,
                onClick = onClick,
                selected = selected,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}


@Composable
fun PagedCountries(
    countries: LazyPagingItems<Country>,
    onClick: (Country) -> Unit,
) {
    when (countries.loadState.refresh) {
        is LoadState.Loading -> {
            AnimatedVisibility(
                visible = true,
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

        is LoadState.Error -> {
            //refresh
        }

        else -> {
            LazyColumn {
                itemsIndexed(countries) { _, country ->
                    country?.let {
                        CountryItem(country = it, onClick = onClick)
                    }
                }
            }
        }
    }
}

@Composable
fun CountryItem(country: Country, onClick: (Country) -> Unit) {
    Row(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .clickable(
                enabled = true,
                onClick = { onClick(country) },
                role = Role.Button,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(
                text = country.code.orEmpty(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                text = country.name.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
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

@Composable
@Preview
fun CountryItemPreview() {
    CountryItem(
        country = Country(
            name = "Nigeria",
            code = "NG"
        ),
        onClick = {}
    )
}
