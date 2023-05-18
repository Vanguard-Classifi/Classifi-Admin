package com.khalidtouch.chatme.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.khalidtouch.chatme.database.models.ClassifiAcademicSessionEntity
import com.khalidtouch.chatme.database.models.ClassifiSchoolEntity

data class SchoolWithSessions(
    @Embedded val school: ClassifiSchoolEntity,
    @Relation(
        parentColumn = "schoolId",
        entityColumn = "schoolId",
    )
    val sessions: List<ClassifiAcademicSessionEntity>
)