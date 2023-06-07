package com.khalidtouch.classifiadmin.settings.navigation.preferences

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.khalidtouch.classifiadmin.settings.R
import com.khalidtouch.classifiadmin.settings.navigation.profile.ProfileScreen
import com.khalidtouch.classifiadmin.settings.navigation.settings.SettingsUiState
import com.khalidtouch.classifiadmin.settings.navigation.settings.SettingsViewModel
import com.khalidtouch.core.designsystem.components.SettingItem
import com.khalidtouch.core.designsystem.icons.ClassifiIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreenWrapper(
    settingsViewModel: SettingsViewModel,
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
        Scaffold(
            content = { padding ->
                PreferencesScreen(
                    modifier = Modifier.padding(padding)
                )
            }
        )
    }
}


@Composable
internal fun PreferencesScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>()
){
    val headerStyle = MaterialTheme.typography.titleMedium.copy(
        color = Color.Black.copy(0.8f)
    )
    val textStyle = MaterialTheme.typography.bodyLarge

   LazyColumn(Modifier.fillMaxSize()) {
       darkModeItem(
           headerStyle = headerStyle,
           textStyle = textStyle,
           onSetDarkThemeConfig = {
               settingsViewModel.onDarkThemeConfigDialogStateChange(true)
           }
       )
   }
}


fun LazyListScope.darkModeItem(
    headerStyle: TextStyle,
    textStyle: TextStyle,
    onSetDarkThemeConfig: () -> Unit,
) {
    item {
        Spacer(modifier = Modifier.height(32.dp))

        SettingItem(
            onClick = onSetDarkThemeConfig,
            icon = {
                Icon(
                    painter = painterResource(id = ClassifiIcons.Dark),
                    contentDescription = null
                )
            },
            text = {
                Column {
                    ProvideTextStyle(value = headerStyle) {
                        Text(
                            text = stringResource(id = R.string.dark_mode)
                        )
                    }

                    ProvideTextStyle(value = textStyle) {
                        Text(
                            text = "Click to configure dark theme"
                        )
                    }

                }
            },
            hasEditIcon = false,
        )
    }
}