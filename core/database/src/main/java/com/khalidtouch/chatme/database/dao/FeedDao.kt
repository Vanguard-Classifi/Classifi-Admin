package com.khalidtouch.chatme.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.khalidtouch.chatme.database.models.ClassifiFeedEntity
import com.khalidtouch.chatme.database.relations.FeedWithClasses
import com.khalidtouch.chatme.database.relations.FeedWithComments
import com.khalidtouch.chatme.database.relations.FeedWithLikes
import com.khalidtouch.chatme.database.relations.FeedWithMessages
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveFeedOrIgnore(feed: ClassifiFeedEntity)

    @Update
    suspend fun updateFeed(feed: ClassifiFeedEntity)

    @Delete
    suspend fun deleteFeed(feed: ClassifiFeedEntity)

    @Query(
        value = "select * from ClassifiFeedEntity where feedId = :id"
    )
    fun fetchFeedById(id: Long): Flow<ClassifiFeedEntity?>

    @Query(
        value = "select * from ClassifiFeedEntity where feedId = :feedId"
    )
    fun fetchFeedWithMessages(feedId: Long): Flow<FeedWithMessages>

    @Query(
        value = "select * from ClassifiFeedEntity where feedId = :feedId"
    )
    fun fetchFeedWithComments(feedId: Long): Flow<FeedWithComments>


    @Query(
        value = "select * from ClassifiFeedEntity where feedId = :feedId"
    )
    fun fetchFeedWithLikes(feedId: Long): Flow<FeedWithLikes>


    @Query(
        value = "select * from ClassifiFeedEntity where feedId = :feedId"
    )
    fun fetchFeedWithClasses(feedId: Long): Flow<FeedWithClasses>

}