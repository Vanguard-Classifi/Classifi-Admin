package com.khalidtouch.chatme.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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
    @Transaction
    fun fetchFeedWithMessages(feedId: Long): Flow<FeedWithMessages>

    @Query(
        value = "select * from ClassifiFeedEntity where feedId = :feedId"
    )
    @Transaction
    fun fetchFeedWithComments(feedId: Long): Flow<FeedWithComments>


    @Query(
        value = "select * from ClassifiFeedEntity where feedId = :feedId"
    )
    @Transaction
    fun fetchFeedWithLikes(feedId: Long): Flow<FeedWithLikes>


    @Query(
        value = "select * from ClassifiFeedEntity where feedId = :feedId"
    )
    @Transaction
    fun fetchFeedWithClasses(feedId: Long): Flow<FeedWithClasses>

    @Query(
        value = "select * from ClassifiFeedEntity where feedId in (:feedIds) order by dateCreated desc"
    )
    fun fetchFeedResources(feedIds: Set<Long>): Flow<List<ClassifiFeedEntity>>

}