package com.khalidtouch.chatme.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.khalidtouch.chatme.database.models.ClassifiFeedEntity
import com.khalidtouch.chatme.database.models.ClassifiLikeEntity


data class FeedWithLikes(
    @Embedded val feed: ClassifiFeedEntity,
    @Relation(
        parentColumn = "feedId",
        entityColumn = "feedId"
    )
    val likes: List<ClassifiLikeEntity>,
)