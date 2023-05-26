package com.khalidtouch.classifiadmin.settings.navigation.settings


import com.khalidtouch.classifiadmin.settings.navigation.profile.ProfileData

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Success(val data: SettingsData) : SettingsUiState
}


data class SettingsData(
    val profileData: ProfileData?,
)

