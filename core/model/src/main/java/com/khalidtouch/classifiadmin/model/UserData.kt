package com.khalidtouch.classifiadmin.model

data class UserData(
    val shouldHideOnboarding: Boolean,
    val shouldReAuthenticate: Boolean,
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
    val themeBrand: ThemeBrand,
    val likedFeeds: Set<Long>,
    val assignedFeeds: Set<Long>,
    val feedData: FeedData,
    val userId: Long,
    val userName: String,
    val profileImage: String,
    val role: UserRole,
    val userEmail: String,
    val schoolId: Long,
)