package com.khalidtouch.classifiadmin.settings.navigation.preferences

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.khalidtouch.classifiadmin.settings.navigation.profile.ProfileScreen
import com.khalidtouch.classifiadmin.settings.navigation.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreenWrapper(
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
){
    Text("Preference Screen")
}