package com.khalidtouch.classifiadmin.data.repository

import com.khalidtouch.chatme.database.dao.CommentDao
import com.khalidtouch.chatme.domain.repository.CommentRepository
import com.khalidtouch.classifiadmin.data.mapper.ModelEntityMapper
import com.khalidtouch.classifiadmin.data.mapper.orEmpty
import com.khalidtouch.classifiadmin.model.classifi.ClassifiComment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

class OfflineFirstCommentRepository @Inject constructor(
    private val modelMapper: ModelEntityMapper,
    private val commentDao: CommentDao,
) : CommentRepository {
    override suspend fun saveComment(comment: ClassifiComment) {
        commentDao.saveCommentOrIgnore(modelMapper.commentModelToEntity(comment)!!)
    }

    override suspend fun updateComment(comment: ClassifiComment) {
        commentDao.updateComment(modelMapper.commentModelToEntity(comment)!!)
    }

    override suspend fun deleteComment(comment: ClassifiComment) {
        commentDao.deleteComment(modelMapper.commentModelToEntity(comment)!!)
    }

    override fun fetchCommentById(commentId: Long): Flow<ClassifiComment?> = flow {
        commentDao.fetchCommentById(commentId).collect {
            val comment = modelMapper.commentEntityToModel(it)
            emit(comment)
        }
    }

    override fun fetchCommentsByFeed(feedId: Long): Flow<List<ClassifiComment>> = flow {
        commentDao.fetchCommentsByFeed(feedId).collect {
            val comments = it.map { comment -> modelMapper.commentEntityToModel(comment)!! }
            emit(comments)
        }
    }

    override fun fetchCommentWithLikes(commentId: Long): Flow<ClassifiComment?> = flow {
        commentDao.fetchCommentWithLikes(commentId).collect {
            emit(
                ClassifiComment(
                    commentId = it?.comment?.commentId.orEmpty(),
                    messages = emptyList(),
                    likes = it?.likes?.map { like -> modelMapper.likeEntityToModel(like)!! }
                        ?: emptyList(),
                    feedId = it?.comment?.feedId.orEmpty(),
                    dateCreated = it?.comment?.dateCreated ?: LocalDateTime.now(),
                    userId = it?.comment?.userId.orEmpty(),
                )
            )
        }
    }

    override fun fetchCommentWithMessages(commentId: Long): Flow<ClassifiComment?> = flow {
        commentDao.fetchCommentWithMessages(commentId).collect {
            emit(
                ClassifiComment(
                    commentId = it?.comment?.commentId.orEmpty(),
                    likes = emptyList(),
                    messages = it?.messages?.map { message -> modelMapper.messageEntityToModel(message)!! }
                        ?: emptyList(),
                    feedId = it?.comment?.feedId.orEmpty(),
                    dateCreated = it?.comment?.dateCreated ?: LocalDateTime.now(),
                    userId = it?.comment?.userId.orEmpty(),
                )
            )
        }
    }
}