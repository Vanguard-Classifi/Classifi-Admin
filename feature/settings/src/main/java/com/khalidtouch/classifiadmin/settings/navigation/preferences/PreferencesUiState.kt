package com.khalidtouch.classifiadmin.settings.navigation.preferences

import com.khalidtouch.classifiadmin.model.UserData

sealed interface PreferencesUiState {
    object Loading: PreferencesUiState
    data class Success(val data: UserData): PreferencesUiState
}

