package com.vanguard.classifiadmin.data.preferences

import com.vanguard.classifiadmin.domain.helpers.Resource

interface PrefDatastore {
    fun saveCurrentUserIdPref(userId: String)
    fun saveCurrentUsernamePref(username: String)
    fun saveCurrentSchoolIdPref(schoolId: String)
    fun saveCurrentSchoolNamePref(schoolName: String)

    val currentUserIdPref: Resource<String?>
    val currentUsernamePref: Resource<String?>
    val currentSchoolIdPref: Resource<String?>
    val currentSchoolNamePref: Resource<String?>
}