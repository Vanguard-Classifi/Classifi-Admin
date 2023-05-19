package com.khalidtouch.chatme.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.khalidtouch.chatme.database.models.ClassifiFeedEntity
import com.khalidtouch.chatme.database.models.ClassifiUserEntity



data class UserWithFeeds(
    @Embedded val user: ClassifiUserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "creatorId"
    )
    val feeds: List<ClassifiFeedEntity>,
)