package com.khalidtouch.chatme.database.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation
import com.khalidtouch.chatme.database.models.ClassifiClassEntity
import com.khalidtouch.chatme.database.models.ClassifiUserEntity

@Entity(
    primaryKeys = ["userId", "classId"],
    foreignKeys = [
        ForeignKey(
            entity = ClassifiUserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
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
        Index(value = ["userId"]),
        Index(value = ["classId"]),
    ]
)
data class UsersWithClassesCrossRef(
    val userId: Long,
    val classId: Long,
)


data class UserWithClasses(
    @Embedded val user: ClassifiUserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "classId",
        associateBy = Junction(
            value = UsersWithClassesCrossRef::class,
            parentColumn = "userId",
            entityColumn = "classId"
        )
    )
    val classes: List<ClassifiClassEntity>,
)


data class ClassWithUsers(
    @Embedded val classifiClass: ClassifiClassEntity,
    @Relation(
        parentColumn = "classId",
        entityColumn = "userId",
        associateBy = Junction(
            value = UsersWithClassesCrossRef::class,
            parentColumn = "classId",
            entityColumn = "userId"
        )
    )
    val users: List<ClassifiUserEntity>,
)