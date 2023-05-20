package com.khalidtouch.chatme.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.khalidtouch.classifiadmin.model.DarkThemeConfig
import com.khalidtouch.classifiadmin.model.ThemeBrand
import com.khalidtouch.classifiadmin.model.UserData
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class ClassifiPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val TAG = "ClassifiPref"

    val userData = userPreferences.data
        .map {
            UserData(
                shouldHideOnboarding = it.shouldHideOnboarding,
                useDynamicColor = it.useDynamicColor,
                darkThemeConfig = when (it.darkThemeConfig) {
                    null,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                    DarkThemeConfigProto.UNRECOGNIZED,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM ->
                        DarkThemeConfig.FOLLOW_SYSTEM

                    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT ->
                        DarkThemeConfig.LIGHT

                    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK ->
                        DarkThemeConfig.DARK
                },
                themeBrand = when (it.themeBrand) {
                    null,
                    ThemeBrandProto.THEME_BRAND_UNSPECIFIED,
                    ThemeBrandProto.UNRECOGNIZED,
                    ThemeBrandProto.THEME_BRAND_DEFAULT ->
                        ThemeBrand.DEFAULT

                    ThemeBrandProto.THEME_BRAND_ANDROID ->
                        ThemeBrand.ANDROID
                },
                likedFeeds = it.likedFeedsMap.keys,
                assignedFeeds = it.assignedFeedsMap.keys,
            )
        }


    suspend fun setLikedFeedIds(feedIds: Set<Long>) {
        try {
            userPreferences.updateData { pref ->
                pref.copy {
                    likedFeeds.clear()
                    likedFeeds.putAll(feedIds.associateWith { true })
                    updateShouldHideOnboardingIfNecessary()
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(this.TAG, "setLikedFeedIds: Failed to update the liked feeds")
        }
    }

    suspend fun setAssignedFeedIds(feedIds: Set<Long>) {
        try {
            userPreferences.updateData { pref ->
                pref.copy {
                    assignedFeeds.clear()
                    assignedFeeds.putAll(feedIds.associateWith { true })
                    updateShouldHideOnboardingIfNecessary()
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(this.TAG, "setLikedFeedIds: Failed to update the assigned feeds")
        }
    }

    suspend fun toggleLikedFeedId(feedId: Long, liked: Boolean) {
        try {
            userPreferences.updateData { pref ->
                pref.copy {
                    if (liked) {
                        likedFeeds.put(feedId, true)
                    } else {
                        likedFeeds.remove(feedId)
                    }
                    updateShouldHideOnboardingIfNecessary()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(this.TAG, "setLikedFeedIds: Failed to update the assigned feeds")
        }
    }

    suspend fun setThemeBrand(theme: ThemeBrand) {
        userPreferences.updateData { pref ->
            pref.copy {
                this.themeBrand = when (theme) {
                    ThemeBrand.DEFAULT -> ThemeBrandProto.THEME_BRAND_DEFAULT
                    ThemeBrand.ANDROID -> ThemeBrandProto.THEME_BRAND_ANDROID
                }
            }
        }
    }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferences.updateData { pref ->
            pref.copy {
                this.useDynamicColor = useDynamicColor
            }
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.updateData { pref ->
            pref.copy {
                this.darkThemeConfig = when (darkThemeConfig) {
                    DarkThemeConfig.FOLLOW_SYSTEM ->
                        DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM

                    DarkThemeConfig.LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                    DarkThemeConfig.DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                }
            }
        }
    }

    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        userPreferences.updateData { pref ->
            pref.copy {
                this.shouldHideOnboarding = shouldHideOnboarding
            }
        }
    }

}

private fun UserPreferencesKt.Dsl.updateShouldHideOnboardingIfNecessary() {
    if (likedFeeds.isEmpty() && assignedFeeds.isEmpty()) {
        shouldHideOnboarding = false
    }
}