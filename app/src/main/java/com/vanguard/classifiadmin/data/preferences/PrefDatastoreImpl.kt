package com.vanguard.classifiadmin.data.preferences

import com.vanguard.classifiadmin.domain.helpers.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefDatastoreImpl @Inject constructor(): PrefDatastore {
    override fun saveCurrentUserIdPref(userId: String) {
        TODO("Not yet implemented")
    }

    override fun saveCurrentUsernamePref(username: String) {
        TODO("Not yet implemented")
    }

    override fun saveCurrentSchoolIdPref(schoolId: String) {
        TODO("Not yet implemented")
    }

    override fun saveCurrentSchoolNamePref(schoolName: String) {
        TODO("Not yet implemented")
    }

    override val currentUserIdPref: Resource<String?>
        get() = TODO("Not yet implemented")
    override val currentUsernamePref: Resource<String?>
        get() = TODO("Not yet implemented")
    override val currentSchoolIdPref: Resource<String?>
        get() = TODO("Not yet implemented")
    override val currentSchoolNamePref: Resource<String?>
        get() = TODO("Not yet implemented")
}