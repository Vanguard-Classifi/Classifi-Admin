package com.khalidtouch.chatme.database.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.khalidtouch.chatme.database.models.ClassifiClassEntity
import com.khalidtouch.chatme.database.models.ClassifiUserEntity

@Entity(primaryKeys = ["userId", "classId"])
data class UsersWithClassesCrossRef(
    val userId: Long,
    val classId: Long,
)


data class UserWithClasses(
    @Embedded val user: ClassifiUserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "classId",
        associateBy = Junction(UsersWithClassesCrossRef::class)
    )
    val classes: List<ClassifiClassEntity>,
)


data class ClassWithUsers(
    @Embedded val classifiClass: ClassifiClassEntity,
    @Relation(
        parentColumn = "classId",
        entityColumn = "userId",
        associateBy = Junction(UsersWithClassesCrossRef::class)
    )
    val users: List<ClassifiUserEntity>,
)