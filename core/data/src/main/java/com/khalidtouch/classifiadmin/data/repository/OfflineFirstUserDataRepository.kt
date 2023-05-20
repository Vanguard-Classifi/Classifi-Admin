package com.khalidtouch.classifiadmin.data.repository

import com.khalidtouch.chatme.datastore.ClassifiPreferencesDataSource
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.classifiadmin.model.DarkThemeConfig
import com.khalidtouch.classifiadmin.model.ThemeBrand
import com.khalidtouch.classifiadmin.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineFirstUserDataRepository @Inject constructor(
    private val classifiPreferenceDataSource: ClassifiPreferencesDataSource,
) : UserDataRepository {
    override val userData: Flow<UserData>
        get() = classifiPreferenceDataSource.userData

    override suspend fun setAssignedFeedIds(assignedFeedIds: Set<Long>) {
        classifiPreferenceDataSource.setAssignedFeedIds(assignedFeedIds)
    }

    override suspend fun setLikedFeeds(likedFeedIds: Set<Long>) {
        classifiPreferenceDataSource.setLikedFeedIds(likedFeedIds)
    }

    override suspend fun toggleLikedFeedId(feedId: Long, liked: Boolean) {
        classifiPreferenceDataSource.toggleLikedFeedId(feedId, liked)
    }


    override suspend fun setThemeBrand(theme: ThemeBrand) {
        classifiPreferenceDataSource.setThemeBrand(theme)
    }

    override suspend fun setDarkThemeConfig(darkThemedConfig: DarkThemeConfig) {
        classifiPreferenceDataSource.setDarkThemeConfig(darkThemedConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        classifiPreferenceDataSource.setDynamicColorPreference(useDynamicColor)
    }

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        classifiPreferenceDataSource.setShouldHideOnboarding(shouldHideOnboarding)
    }
}