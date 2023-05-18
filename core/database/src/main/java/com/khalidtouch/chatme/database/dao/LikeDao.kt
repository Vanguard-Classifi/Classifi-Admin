package com.khalidtouch.chatme.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khalidtouch.chatme.database.models.ClassifiLikeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LikeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveLikeOrIgnore(like: ClassifiLikeEntity)

    @Delete
    suspend fun deleteLike(like: ClassifiLikeEntity)

    @Query(
        value = "select * from ClassifiLikeEntity where feedId like :feedId"
    )
    fun fetchLikesByFeed(feedId: Long): Flow<List<ClassifiLikeEntity>>

    @Query(
        value = "select * from ClassifiLikeEntity where commentId like :commentId"
    )
    fun fetchLikesByComment(commentId: Long): Flow<List<ClassifiLikeEntity>>

    @Query(
        value = "select * from ClassifiLikeEntity where userId = :userId and feedId = :feedId"
    )
    fun fetchLikeByUserAndFeed(userId: Long, feedId: Long): Flow<ClassifiLikeEntity?>

    @Query(
        value = "select * from ClassifiLikeEntity where userId = :userId and commentId = :commentId"
    )
    fun fetchLikeByUserAndComment(userId: Long, commentId: Long): Flow<ClassifiLikeEntity?>
}