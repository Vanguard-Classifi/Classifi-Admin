package com.khalidtouch.chatme.domain.repository

import com.khalidtouch.classifiadmin.model.classifi.ClassifiFeed
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    suspend fun saveFeed(feed: ClassifiFeed)

    suspend fun updateFeed(feed: ClassifiFeed)

    suspend fun deleteFeed(feed: ClassifiFeed)

    suspend fun fetchFeedById(feedId: Long): Flow<ClassifiFeed?>

    fun fetchFeedWithMessages(feedId: Long): Flow<ClassifiFeed?>

    fun fetchFeedWithComments(feedId: Long): Flow<ClassifiFeed?>

    fun fetchFeedWithLikes(feedId: Long): Flow<ClassifiFeed?>

    fun fetchFeedWithClasses(feedId: Long): Flow<ClassifiFeed?>
}