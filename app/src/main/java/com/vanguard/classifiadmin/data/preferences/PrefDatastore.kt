package com.vanguard.classifiadmin.data.preferences

import kotlinx.coroutines.flow.Flow

interface PrefDatastore {
    fun saveCurrentUserIdPref(userId: String, onResult: (Boolean) -> Unit)
    fun saveCurrentUsernamePref(username: String, onResult: (Boolean) -> Unit)
    fun saveCurrentUserEmailPref(email: String, onResult: (Boolean) -> Unit)
    fun saveCurrentSchoolIdPref(schoolId: String, onResult: (Boolean) -> Unit)
    fun saveCurrentSchoolNamePref(schoolName: String, onResult: (Boolean) -> Unit)
    fun saveCurrentProfileImagePref(downloadUrl: String, onResult: (Boolean) -> Unit)
    fun saveCurrentUserRole(role: String, onResult: (Boolean) -> Unit)

    val currentUserIdPref: Flow<String?>
    val currentUsernamePref:  Flow<String?>
    val currentUserEmailPref:  Flow<String?>
    val currentSchoolIdPref:  Flow<String?>
    val currentSchoolNamePref:  Flow<String?>
    val currentProfileImagePref:  Flow<String?>
    val currentUserRole: Flow<String?>
}