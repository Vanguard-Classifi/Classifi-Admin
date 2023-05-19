package com.khalidtouch.chatme.database.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation
import com.khalidtouch.chatme.database.models.ClassifiSchoolEntity
import com.khalidtouch.chatme.database.models.ClassifiUserEntity
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool


@Entity(
    primaryKeys = ["userId", "schoolId"],
    foreignKeys = [
        ForeignKey(
            entity = ClassifiUserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ClassifiSchoolEntity::class,
            parentColumns = ["schoolId"],
            childColumns = ["schoolId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["schoolId"]),
    ]
)
data class UsersWithSchoolsCrossRef(
    val userId: Long,
    val schoolId: Long,
)

data class UserWithSchools(
    @Embedded val user: ClassifiUserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "schoolId",
        associateBy = Junction(
            value = UsersWithSchoolsCrossRef::class,
            parentColumn = "userId",
            entityColumn = "schoolId"
        )
    )
    val schools: List<ClassifiSchoolEntity>,
)


data class SchoolWithUsers(
    @Embedded val school: ClassifiSchoolEntity,
    @Relation(
        parentColumn = "schoolId",
        entityColumn = "userId",
        associateBy = Junction(
            value = UsersWithSchoolsCrossRef::class,
            parentColumn = "schoolId",
            entityColumn = "userId"
        )
    )
    val users: List<ClassifiUserEntity>,
)