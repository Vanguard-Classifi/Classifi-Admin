package com.khalidtouch.classifiadmin.data.repository

import android.util.Log
import com.khalidtouch.chatme.datastore.ClassifiPreferencesDataSource
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.classifiadmin.model.DarkThemeConfig
import com.khalidtouch.classifiadmin.model.FeedMessage
import com.khalidtouch.classifiadmin.model.ThemeBrand
import com.khalidtouch.classifiadmin.model.UserData
import com.khalidtouch.classifiadmin.model.UserRole
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineFirstUserDataRepository @Inject constructor(
    private val classifiPreferenceDataSource: ClassifiPreferencesDataSource,
) : UserDataRepository {
    val TAG = "UserData"
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

    override suspend fun enqueueMediaMessage(message: FeedMessage) {
        classifiPreferenceDataSource.enqueueMediaMessage(message)
        Log.e(TAG, "enqueueMediaMessage: onEnqueue media message")
    }

    override suspend fun enqueueTextMessage(message: FeedMessage) {
        classifiPreferenceDataSource.enqueueTextMessage(message)
    }

    override suspend fun deleteMessageById(messageId: Long) {
        classifiPreferenceDataSource.deleteMessageById(messageId)
    }

    override suspend fun clearAllMessages() {
        classifiPreferenceDataSource.clearAllMessages()
    }

    override suspend fun setUserId(id: Long) {
        classifiPreferenceDataSource.setUserId(id)
    }

    override suspend fun setUsername(name: String) {
      classifiPreferenceDataSource.setUsername(name)
    }

    override suspend fun setUserEmail(email: String) {
        classifiPreferenceDataSource.setUserEmail(email)
    }

    override suspend fun setUserProfileImage(imageUrl: String) {
       classifiPreferenceDataSource.setUserProfileImage(imageUrl)
    }

    override suspend fun setUserRole(userRole: UserRole) {
       classifiPreferenceDataSource.setUserRole(userRole)
    }
}