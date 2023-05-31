package com.khalidtouch.chatme.network

import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool

interface SchoolNetworkDataSource {

    fun saveSchool(school: ClassifiSchool)

    fun registerUserWithSchool(userId: Long, schoolId: Long, schoolName: String)

    fun updateSchool(school: ClassifiSchool)

    fun deleteSchool(school: ClassifiSchool)

    fun fetchSchoolById(schoolId: Long, callback: (ClassifiSchool?) -> Unit)

}