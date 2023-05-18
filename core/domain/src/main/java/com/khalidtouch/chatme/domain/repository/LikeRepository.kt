package com.khalidtouch.chatme.domain.repository

import com.khalidtouch.classifiadmin.model.classifi.ClassifiLike
import kotlinx.coroutines.flow.Flow

interface LikeRepository {
    suspend fun saveLike(like: ClassifiLike)

    suspend fun deleteLike(like: ClassifiLike)

    fun fetchLikesByFeed(feedId: Long): Flow<List<ClassifiLike>>

    fun fetchLikesByComment(commentId: Long): Flow<List<ClassifiLike>>

    fun fetchLikeByUserAndFeed(userId: Long, feedId: Long): Flow<ClassifiLike?>

    fun fetchLikeByUserAndComment(userId: Long, commentId: Long): Flow<ClassifiLike?>

}