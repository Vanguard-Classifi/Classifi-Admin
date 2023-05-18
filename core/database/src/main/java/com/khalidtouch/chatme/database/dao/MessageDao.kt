package com.khalidtouch.chatme.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.khalidtouch.chatme.database.models.ClassifiMessageEntity
import com.khalidtouch.classifiadmin.model.classifi.ClassifiMessage
import kotlinx.coroutines.flow.Flow


@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveMessageOrIgnore(message: ClassifiMessageEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveMessagesOrIgnore(messages: List<ClassifiMessageEntity>)

    @Update
    suspend fun updateMessage(message: ClassifiMessageEntity)

    @Delete
    suspend fun deleteMessage(message: ClassifiMessageEntity)

    @Query(
        value = "select * from ClassifiMessageEntity where messageId = :id"
    )
    fun fetchMessageById(id: Long): Flow<ClassifiMessageEntity?>

    @Query(
        value = "select * from ClassifiMessageEntity where feedId = :feedId"
    )
    fun fetchMessagesByFeed(feedId: Long): Flow<List<ClassifiMessageEntity>>

}