package com.khalidtouch.classifiadmin.model.classifi

import kotlinx.datetime.LocalDateTime
import java.util.Date

data class ClassifiAcademicSession(
    val sessionId: Long,
    val sessionName: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val schoolId: Long,
)

