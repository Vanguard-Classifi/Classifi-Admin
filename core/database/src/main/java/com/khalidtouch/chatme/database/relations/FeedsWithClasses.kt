package com.khalidtouch.chatme.database.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation
import com.khalidtouch.chatme.database.models.ClassifiClassEntity
import com.khalidtouch.chatme.database.models.ClassifiFeedEntity

@Entity(
    primaryKeys = ["feedId", "classId"],
    foreignKeys = [
        ForeignKey(
            entity = ClassifiFeedEntity::class,
            parentColumns = ["feedId"],
            childColumns = ["feedId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ClassifiClassEntity::class,
            parentColumns = ["classId"],
            childColumns = ["classId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["feedId"]),
        Index(value = ["classId"]),
    ]
)
data class FeedsWithClassesCrossRef(
    val feedId: Long,
    val classId: Long,
)

data class FeedWithClasses(
    @Embedded val feed: ClassifiFeedEntity,
    @Relation(
        parentColumn = "feedId",
        entityColumn = "classId",
        associateBy = Junction(
            value = FeedsWithClassesCrossRef::class,
            parentColumn = "feedId",
            entityColumn = "classId"
        )
    )
    val classes: List<ClassifiClassEntity>,
)

data class ClassWithFeeds(
    @Embedded val classifiClass: ClassifiClassEntity,
    @Relation(
        parentColumn = "classId",
        entityColumn = "feedId",
        associateBy = Junction(
            value = FeedsWithClassesCrossRef::class,
            parentColumn = "classId",
            entityColumn = "feedId"
        )
    )
    val feeds: List<ClassifiFeedEntity>,
)