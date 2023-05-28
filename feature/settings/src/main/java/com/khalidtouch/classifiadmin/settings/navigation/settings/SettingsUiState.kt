package com.khalidtouch.classifiadmin.settings.navigation.settings


import com.khalidtouch.classifiadmin.model.DarkThemeConfig
import com.khalidtouch.classifiadmin.model.ThemeBrand
import com.khalidtouch.classifiadmin.settings.navigation.profile.ProfileData

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Success(val data: SettingsData) : SettingsUiState
}


data class SettingsData(
    val profileData: ProfileData?,
    val userId: Long,
    val hasUserProfileUpdated: Boolean,
    val darkThemeSettings: DarkThemeConfigSettings,
)

data class DarkThemeConfigSettings(
    val brand: ThemeBrand,
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
)
