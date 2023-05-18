package com.khalidtouch.chatme.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.khalidtouch.chatme.database.models.ClassifiCommentEntity
import com.khalidtouch.chatme.database.models.ClassifiFeedEntity


data class FeedWithComments(
    @Embedded val feed: ClassifiFeedEntity,
    @Relation(
        parentColumn = "feedId",
        entityColumn = "feedId",
    )
    val comments: List<ClassifiCommentEntity>,
)