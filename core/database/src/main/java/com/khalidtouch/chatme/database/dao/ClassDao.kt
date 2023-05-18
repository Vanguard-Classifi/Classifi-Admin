package com.khalidtouch.chatme.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.khalidtouch.chatme.database.models.ClassifiClassEntity
import com.khalidtouch.chatme.database.relations.ClassWithFeeds
import com.khalidtouch.chatme.database.relations.ClassWithUsers
import kotlinx.coroutines.flow.Flow


@Dao
interface ClassDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveClassOrIgnore(classifiClass: ClassifiClassEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveClassesOrIgnore(classes: List<ClassifiClassEntity>)

    @Update
    suspend fun updateClass(classifiClass: ClassifiClassEntity)

    @Delete
    suspend fun deleteClass(classifiClass: ClassifiClassEntity)

    @Query(
        value = "select * from ClassifiClassEntity where classId = :id"
    )
    fun fetchClassById(id: Long): Flow<ClassifiClassEntity?>

    @Query(
        value = "select * from ClassifiClassEntity where schoolId like :schoolId order by className asc"
    )
    fun fetchAllClassesBySchool(schoolId: Long): Flow<List<ClassifiClassEntity>>

    @Query(
        value = "select * from ClassifiClassEntity where classId = :classId"
    )
    fun fetchClassWithFeeds(classId: Long): Flow<ClassWithFeeds>

    @Query(
        value = "select * from ClassifiClassEntity where classId = :classId"
    )
    fun fetchClassWithUsers(classId: Long): Flow<ClassWithUsers>
}