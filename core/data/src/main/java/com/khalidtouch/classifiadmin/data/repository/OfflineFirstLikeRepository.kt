package com.khalidtouch.classifiadmin.data.repository

import com.khalidtouch.chatme.database.dao.LikeDao
import com.khalidtouch.chatme.domain.repository.LikeRepository
import com.khalidtouch.classifiadmin.data.mapper.ModelEntityMapper
import com.khalidtouch.classifiadmin.model.classifi.ClassifiLike
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OfflineFirstLikeRepository @Inject constructor(
    private val modelMapper: ModelEntityMapper,
    private val likeDao: LikeDao,
) : LikeRepository {
    override suspend fun saveLike(like: ClassifiLike) {
        likeDao.saveLikeOrIgnore(modelMapper.likeModelToEntity(like)!!)
    }

    override suspend fun deleteLike(like: ClassifiLike) {
        likeDao.deleteLike(modelMapper.likeModelToEntity(like)!!)
    }

    override fun fetchLikesByFeed(feedId: Long): Flow<List<ClassifiLike>> = flow {
        likeDao.fetchLikesByFeed(feedId).collect {
            val likes = it.map { like -> modelMapper.likeEntityToModel(like)!! }
            emit(likes)
        }
    }

    override fun fetchLikesByComment(commentId: Long): Flow<List<ClassifiLike>> = flow {
        likeDao.fetchLikesByComment(commentId).collect {
            val likes = it.map { like -> modelMapper.likeEntityToModel(like)!! }
            emit(likes)
        }
    }

    override fun fetchLikeByUserAndFeed(userId: Long, feedId: Long): Flow<ClassifiLike?> = flow {
        likeDao.fetchLikeByUserAndFeed(userId, feedId).collect {
            emit(modelMapper.likeEntityToModel(it))
        }
    }

    override fun fetchLikeByUserAndComment(userId: Long, commentId: Long): Flow<ClassifiLike?> = flow {
        likeDao.fetchLikeByUserAndComment(userId, commentId).collect {
            emit(modelMapper.likeEntityToModel(it))
        }
    }
}