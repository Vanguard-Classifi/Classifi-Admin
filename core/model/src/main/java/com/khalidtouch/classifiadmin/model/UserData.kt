package com.khalidtouch.classifiadmin.model

data class UserData(
    val shouldHideOnboarding: Boolean,
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
    val themeBrand: ThemeBrand,
)