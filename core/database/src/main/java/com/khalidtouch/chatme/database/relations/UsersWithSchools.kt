package com.khalidtouch.chatme.database.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.khalidtouch.chatme.database.models.ClassifiSchoolEntity
import com.khalidtouch.chatme.database.models.ClassifiUserEntity
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool


@Entity(primaryKeys = ["userId", "schoolId"])
data class UsersWithSchoolsCrossRef(
    val userId: Long,
    val schoolId: Long,
)


data class UserWithSchools(
    @Embedded val user: ClassifiUserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "schoolId",
        associateBy = Junction(UsersWithSchoolsCrossRef::class)
    )
    val schools: List<ClassifiSchool>,
)


data class SchoolWithUsers(
    @Embedded val school: ClassifiSchoolEntity,
    @Relation(
        parentColumn = "schoolId",
        entityColumn = "userId",
        associateBy = Junction(UsersWithSchoolsCrossRef::class)
    )
    val users: List<ClassifiUserEntity>,
)