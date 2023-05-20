package com.khalidtouch.classifiadmin.data.repository

import com.khalidtouch.chatme.database.dao.FeedDao
import com.khalidtouch.chatme.domain.repository.FeedQuery
import com.khalidtouch.chatme.domain.repository.FeedRepository
import com.khalidtouch.classifiadmin.data.mapper.ModelEntityMapper
import com.khalidtouch.classifiadmin.data.mapper.ModelEntityMapperImpl
import com.khalidtouch.classifiadmin.data.mapper.orEmpty
import com.khalidtouch.classifiadmin.model.classifi.ClassifiFeed
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class OfflineFirstFeedRepository @Inject constructor(
    private val modelMapper: ModelEntityMapper,
    private val feedDao: FeedDao,
) : FeedRepository {
    override suspend fun saveFeed(feed: ClassifiFeed) {
        feedDao.saveFeedOrIgnore(modelMapper.feedModelToEntity(feed)!!)
    }

    override suspend fun updateFeed(feed: ClassifiFeed) {
        feedDao.updateFeed(modelMapper.feedModelToEntity(feed)!!)
    }

    override suspend fun deleteFeed(feed: ClassifiFeed) {
        feedDao.deleteFeed(modelMapper.feedModelToEntity(feed)!!)
    }

    override suspend fun fetchFeedById(feedId: Long): Flow<ClassifiFeed?> = flow {
        feedDao.fetchFeedById(feedId).collect {
            val feed = modelMapper.feedEntityToModel(it)
            emit(feed)
        }
    }

    override fun fetchFeedWithMessages(feedId: Long): Flow<ClassifiFeed?> = flow {
        feedDao.fetchFeedWithMessages(feedId).collect {
            emit(
                ClassifiFeed(
                    feedId = it.feed.feedId,
                    creator = ClassifiUser(
                        userId = it.feed.creatorId.orEmpty(),
                    ),
                    messages = it.messages.map { message -> modelMapper.messageEntityToModel(message)!! },
                    likes = emptyList(),
                    comments = emptyList(),
                    classes = emptyList(),
                    dateCreated = it.feed.dateCreated ?: LocalDateTime.now(),
                )
            )
        }
    }

    override fun fetchFeedWithComments(feedId: Long): Flow<ClassifiFeed?> = flow {
        feedDao.fetchFeedWithComments(feedId).collect {
            emit(
                ClassifiFeed(
                    feedId = it.feed.feedId,
                    creator = ClassifiUser(
                        userId = it.feed.creatorId.orEmpty(),
                    ),
                    messages = emptyList(),
                    likes = emptyList(),
                    comments = it.comments.map { comment -> modelMapper.commentEntityToModel(comment)!! },
                    classes = emptyList(),
                    dateCreated = it.feed.dateCreated ?: LocalDateTime.now(),
                )
            )
        }
    }

    override fun fetchFeedWithLikes(feedId: Long): Flow<ClassifiFeed?> = flow {
        feedDao.fetchFeedWithLikes(feedId).collect {
            emit(
                ClassifiFeed(
                    feedId = it.feed.feedId,
                    creator = ClassifiUser(
                        userId = it.feed.creatorId.orEmpty(),
                    ),
                    messages = emptyList(),
                    likes = it.likes.map { like -> modelMapper.likeEntityToModel(like)!! },
                    comments = emptyList(),
                    classes = emptyList(),
                    dateCreated = it.feed.dateCreated ?: LocalDateTime.now(),
                )
            )
        }
    }

    override fun fetchFeedWithClasses(feedId: Long): Flow<ClassifiFeed?> = flow {
        feedDao.fetchFeedWithClasses(feedId).collect {
            emit(
                ClassifiFeed(
                    feedId = it.feed.feedId,
                    creator = ClassifiUser(
                        userId = it.feed.creatorId.orEmpty(),
                    ),
                    messages = emptyList(),
                    likes = emptyList(),
                    comments = emptyList(),
                    classes = it.classes.map { c -> modelMapper.classEntityToModel(c)!! },
                    dateCreated = it.feed.dateCreated ?: LocalDateTime.now(),
                )
            )
        }
    }

    override fun fetchFeedResources(query: FeedQuery): Flow<List<ClassifiFeed>> {
        return feedDao.fetchFeedResources(query.filterByFeedIds ?: setOf(-1L)).map {
            it.map { feed -> modelMapper.feedEntityToModel(feed)!! }
        }
    }
}