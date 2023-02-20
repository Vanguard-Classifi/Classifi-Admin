package com.vanguard.classifiadmin.data.preferences

import com.vanguard.classifiadmin.domain.helpers.Resource
import kotlinx.coroutines.flow.Flow

interface PrefDatastore {
    fun saveCurrentUserIdPref(userId: String, onResult: (Boolean) -> Unit)
    fun saveCurrentUsernamePref(username: String, onResult: (Boolean) -> Unit)
    fun saveCurrentUserEmailPref(email: String, onResult: (Boolean) -> Unit)
    fun saveCurrentSchoolIdPref(schoolId: String, onResult: (Boolean) -> Unit)
    fun saveCurrentSchoolNamePref(schoolName: String, onResult: (Boolean) -> Unit)

    val currentUserIdPref: Flow<String?>
    val currentUsernamePref:  Flow<String?>
    val currentUserEmailPref:  Flow<String?>
    val currentSchoolIdPref:  Flow<String?>
    val currentSchoolNamePref:  Flow<String?>
}