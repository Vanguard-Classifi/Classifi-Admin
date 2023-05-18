package com.khalidtouch.chatme.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.khalidtouch.chatme.database.models.ClassifiSchoolEntity
import com.khalidtouch.chatme.database.relations.SchoolWithClasses
import com.khalidtouch.chatme.database.relations.SchoolWithSessions
import com.khalidtouch.chatme.database.relations.SchoolWithUsers

@Dao
interface SchoolDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveSchoolOrIgnore(school: ClassifiSchoolEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveSchoolsOrIgnore(schools: List<ClassifiSchoolEntity>)

    @Update
    suspend fun updateSchool(school: ClassifiSchoolEntity)

    @Delete
    suspend fun deleteSchool(school: ClassifiSchoolEntity)

    @Query(
        value = "select * from ClassifiSchoolEntity where schoolId = :id"
    )
    suspend fun fetchSchoolById(id: Long): ClassifiSchoolEntity?

    @Query(
        value = "select * from ClassifiSchoolEntity where schoolId = :schoolId"
    )
    suspend fun fetchSchoolWithSessions(schoolId: Long): SchoolWithSessions?

    @Query(
        value = "select * from ClassifiSchoolEntity where schoolId = :schoolId"
    )
    suspend fun fetchSchoolWithClasses(schoolId: Long): SchoolWithClasses?

    @Query(
        value = "select * from ClassifiSchoolEntity where schoolId = :schoolId"
    )
    suspend fun fetchSchoolWithUsers(schoolId: Long): SchoolWithUsers?
}