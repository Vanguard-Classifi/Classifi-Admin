package com.khalidtouch.chatme.domain.repository

import com.khalidtouch.classifiadmin.model.classifi.ClassifiComment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    suspend fun saveComment(comment: ClassifiComment)

    suspend fun updateComment(comment: ClassifiComment)

    suspend fun deleteComment(comment: ClassifiComment)

    fun fetchCommentById(commentId: Long): Flow<ClassifiComment?>

    fun fetchCommentsByFeed(feedId: Long): Flow<List<ClassifiComment>>

    fun fetchCommentWithLikes(commentId: Long): Flow<ClassifiComment?>

    fun fetchCommentWithMessages(commentId: Long): Flow<ClassifiComment?>

}