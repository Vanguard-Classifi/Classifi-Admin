package com.khalidtouch.chatme.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.khalidtouch.chatme.database.models.ClassifiClassEntity
import com.khalidtouch.chatme.database.models.ClassifiSchoolEntity


data class SchoolWithClasses(
    @Embedded val school: ClassifiSchoolEntity,
    @Relation(
        parentColumn = "schoolId",
        entityColumn = "schoolId",
    )
    val classes: List<ClassifiClassEntity>
)