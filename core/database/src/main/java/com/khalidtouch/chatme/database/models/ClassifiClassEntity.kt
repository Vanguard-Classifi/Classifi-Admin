package com.khalidtouch.chatme.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import java.util.Date

@Entity
data class ClassifiClassEntity(
    @PrimaryKey(autoGenerate = true) val classId: Long,
    val schoolId: Long,
    var className: String? = null,
    var classCode: String? = null,
    var dateCreated: LocalDateTime? = null,
)