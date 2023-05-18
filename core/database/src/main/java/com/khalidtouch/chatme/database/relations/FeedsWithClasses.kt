package com.khalidtouch.chatme.database.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.khalidtouch.chatme.database.models.ClassifiClassEntity
import com.khalidtouch.chatme.database.models.ClassifiFeedEntity

@Entity(primaryKeys = ["feedId", "classId"])
data class FeedsWithClassesCrossRef(
    val feedId: Long,
    val classId: Long,
)

data class FeedWithClasses(
    @Embedded val feed: ClassifiFeedEntity,
    @Relation(
        parentColumn = "feedId",
        entityColumn = "classId",
        associateBy = Junction(FeedsWithClassesCrossRef::class)
    )
    val classes: List<ClassifiClassEntity>,
)

data class ClassWithFeeds(
    @Embedded val classifiClass: ClassifiClassEntity,
    @Relation(
        parentColumn = "classId",
        entityColumn = "feedId",
        associateBy = Junction(FeedsWithClassesCrossRef::class)
    )
    val feeds: List<ClassifiFeedEntity>,
)