package com.khalidtouch.chatme.domain.repository

import com.khalidtouch.classifiadmin.model.classifi.ClassifiClass
import kotlinx.coroutines.flow.Flow

interface ClassRepository {
    suspend fun saveClass(classifiClass: ClassifiClass)

    suspend fun saveClasses(classes: List<ClassifiClass>)
    suspend fun updateClass(classifiClass: ClassifiClass)

    suspend fun deleteClass(classifiClass: ClassifiClass)

    fun fetchClassById(classId: Long): Flow<ClassifiClass?>

    fun fetchAllClassesBySchool(schoolId: Long): Flow<List<ClassifiClass>>

    fun fetchClassWithFeeds(classId: Long): Flow<ClassifiClass?>

    fun fetchClassWithUsers(classId: Long): Flow<ClassifiClass?>
}