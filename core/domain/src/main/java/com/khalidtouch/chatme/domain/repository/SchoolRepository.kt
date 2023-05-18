package com.khalidtouch.chatme.domain.repository

import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool

interface SchoolRepository {
    suspend fun saveSchool(school: ClassifiSchool)

    suspend fun saveSchools(schools: List<ClassifiSchool>)

    suspend fun updateSchool(school: ClassifiSchool)

    suspend fun deleteSchool(school: ClassifiSchool)

    suspend fun fetchSchoolById(schoolId: Long): ClassifiSchool?

    suspend fun fetchSchoolWithSessions(schoolId: Long): ClassifiSchool?

    suspend fun fetchSchoolWithClasses(schoolId: Long): ClassifiSchool?

    suspend fun fetchSchoolWithUsers(schoolId: Long): ClassifiSchool?
}