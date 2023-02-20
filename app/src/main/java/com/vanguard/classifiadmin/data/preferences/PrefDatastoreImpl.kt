package com.vanguard.classifiadmin.data.preferences

import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vanguard.classifiadmin.domain.helpers.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

object PrefKeys {
    const val currentUserId = "currentUserId"
    const val currentUsername = "currentUsername"
    const val currentSchoolId = "currentSchoolId"
    const val currentSchoolName = "currentSchoolName"
    const val currentUserEmail = "currentUserEmail"
}

@Singleton
class PrefDatastoreImpl @Inject constructor(
    @ApplicationContext context: Context
) : PrefDatastore, ContextWrapper(context) {
    private val Context.store: DataStore<Preferences> by preferencesDataStore(("classifi.ds"))
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private fun callback(handler: () -> Unit) = Handler(Looper.getMainLooper())
        .post { handler() }


    override fun saveCurrentUserIdPref(userId: String, onResult: (Boolean) -> Unit) {
        try {
            scope.launch {
                store.edit { pref ->
                    pref[stringPreferencesKey(PrefKeys.currentUserId)] = userId
                }
            }.invokeOnCompletion { callback { onResult(true) } }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override fun saveCurrentUsernamePref(username: String, onResult: (Boolean) -> Unit) {
        try {
            scope.launch {
                store.edit { pref ->
                    pref[stringPreferencesKey(PrefKeys.currentUsername)] = username
                }
            }.invokeOnCompletion { callback { onResult(true) } }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override fun saveCurrentUserEmailPref(email: String, onResult: (Boolean) -> Unit) {
        try {
            scope.launch {
                store.edit { pref ->
                    pref[stringPreferencesKey(PrefKeys.currentUserEmail)] = email
                }
            }.invokeOnCompletion { callback { onResult(true) } }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override fun saveCurrentSchoolIdPref(schoolId: String, onResult: (Boolean) -> Unit) {
        try {
            scope.launch {
                store.edit { pref ->
                    pref[stringPreferencesKey(PrefKeys.currentSchoolId)] = schoolId
                }
            }.invokeOnCompletion { callback { onResult(true) } }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override fun saveCurrentSchoolNamePref(schoolName: String, onResult: (Boolean) -> Unit) {
        try {
            scope.launch {
                store.edit { pref ->
                    pref[stringPreferencesKey(PrefKeys.currentSchoolName)] = schoolName
                }
            }.invokeOnCompletion { callback { onResult(true) } }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override val currentUserIdPref: Flow<String?>
        get() {
            val userId = try {
                store.data.map { pref -> pref[stringPreferencesKey(PrefKeys.currentUserId)] }
            } catch (e: Exception) {
                return flowOf("")
            }
            return userId
        }

    override val currentUsernamePref: Flow<String?>
        get() {
            val username = try {
                store.data.map { pref -> pref[stringPreferencesKey(PrefKeys.currentUsername)] }
            } catch (e: Exception) {
                return flowOf("")
            }
            return username
        }
    override val currentUserEmailPref: Flow<String?>
        get() {
            val email = try {
                store.data.map { pref -> pref[stringPreferencesKey(PrefKeys.currentUserEmail)] }
            } catch (e: Exception) {
                return flowOf("")
            }
            return email
        }

    override val currentSchoolIdPref: Flow<String?>
        get() {
            val schoolId = try {
                store.data.map { pref -> pref[stringPreferencesKey(PrefKeys.currentSchoolId)] }
            } catch (e: Exception) {
                return flowOf("")
            }
            return schoolId
        }

    override val currentSchoolNamePref: Flow<String?>
        get() {
            val schoolName = try {
                store.data.map { pref -> pref[stringPreferencesKey(PrefKeys.currentSchoolName)] }
            } catch (e: Exception) {
                return flowOf("")
            }
            return schoolName
        }

}