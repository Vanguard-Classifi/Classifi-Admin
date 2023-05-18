package com.khalidtouch.chatme.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.khalidtouch.chatme.database.models.ClassifiAcademicSessionEntity
import com.khalidtouch.chatme.database.models.ClassifiUserEntity

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveSessionOrIgnore(session: ClassifiAcademicSessionEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveSessionsOrIgnore(sessions: List<ClassifiAcademicSessionEntity>)

    @Update
    suspend fun updateSession(session: ClassifiAcademicSessionEntity)

    @Upsert
    suspend fun updateSessions(sessions: List<ClassifiAcademicSessionEntity>)


    @Delete
    suspend fun deleteSession(session: ClassifiAcademicSessionEntity)

    @Query(
        value = "delete from ClassifiAcademicSessionEntity where sessionId in (:ids)"
    )
    suspend fun deleteSessionsById(ids: List<Long>)

    @Query(
        value = "select * from ClassifiAcademicSessionEntity where sessionId = :id"
    )
    suspend fun fetchSessionById(id: Long): ClassifiAcademicSessionEntity?

}