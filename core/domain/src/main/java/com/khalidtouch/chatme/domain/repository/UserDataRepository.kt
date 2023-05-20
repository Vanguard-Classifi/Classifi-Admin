package com.khalidtouch.chatme.domain.repository

import com.khalidtouch.classifiadmin.model.DarkThemeConfig
import com.khalidtouch.classifiadmin.model.ThemeBrand
import com.khalidtouch.classifiadmin.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>

    suspend fun setAssignedFeedIds(assignedFeedIds: Set<Long>)

    suspend fun setLikedFeeds(likedFeedIds: Set<Long>)

    suspend fun toggleLikedFeedId(feedId: Long, liked: Boolean)

    suspend fun setThemeBrand(theme: ThemeBrand)

    suspend fun setDarkThemeConfig(darkThemedConfig: DarkThemeConfig)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)

}