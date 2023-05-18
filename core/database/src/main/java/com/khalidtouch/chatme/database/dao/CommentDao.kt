package com.khalidtouch.chatme.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.khalidtouch.chatme.database.models.ClassifiCommentEntity
import com.khalidtouch.chatme.database.relations.CommentWithLikes
import com.khalidtouch.chatme.database.relations.CommentWithMessages
import com.khalidtouch.classifiadmin.model.classifi.ClassifiComment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveCommentOrIgnore(comment: ClassifiCommentEntity)

    @Update
    suspend fun updateComment(comment: ClassifiCommentEntity)

    @Delete
    suspend fun deleteComment(comment: ClassifiCommentEntity)

    @Query(
        value = "select * from ClassifiCommentEntity where commentId = :id"
    )
    fun fetchCommentById(id: Long): Flow<ClassifiCommentEntity?>

    @Query(
        value = "select * from ClassifiCommentEntity where feedId = :feedId"
    )
    fun fetchCommentsByFeed(feedId: Long): Flow<List<ClassifiComment>>

    @Query(
        value = "select * from ClassifiCommentEntity where commentId = :commentId"
    )
    fun fetchCommentWithLikes(commentId: Long): Flow<CommentWithLikes?>

    @Query(
        value = "select * from ClassifiCommentEntity where commentId = :commentId"
    )
    fun fetchCommentWithMessages(commentId: Long): Flow<CommentWithMessages?>

}