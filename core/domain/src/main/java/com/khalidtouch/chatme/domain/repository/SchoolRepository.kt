package com.khalidtouch.chatme.domain.repository

import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import kotlinx.coroutines.flow.Flow

interface SchoolRepository {
    suspend fun saveSchool(school: ClassifiSchool)

    suspend fun saveSchools(schools: List<ClassifiSchool>)

    suspend fun registerUserWithSchool(userId: Long, schoolId: Long, schoolName: String)

    suspend fun updateSchool(school: ClassifiSchool)

    suspend fun deleteSchool(school: ClassifiSchool)

    suspend fun deleteAllSchools()

    suspend fun fetchSchoolById(schoolId: Long): ClassifiSchool?

    fun observeSchoolById(schoolId: Long): Flow<ClassifiSchool?>

    suspend fun fetchSchoolWithSessions(schoolId: Long): ClassifiSchool?

    suspend fun fetchSchoolWithClasses(schoolId: Long): ClassifiSchool?
    suspend fun fetchSchoolWithStudents(schoolId: Long): ClassifiSchool?
    suspend fun fetchSchoolWithTeachers(schoolId: Long): ClassifiSchool?
    suspend fun fetchSchoolWithParents(schoolId: Long): ClassifiSchool?
}