package com.khalidtouch.chatme.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import java.util.Date

@Entity
data class ClassifiAcademicSessionEntity(
    @PrimaryKey(autoGenerate = true) val sessionId: Long,
    val schoolId: Long,
    var sessionName: String? = null,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
)