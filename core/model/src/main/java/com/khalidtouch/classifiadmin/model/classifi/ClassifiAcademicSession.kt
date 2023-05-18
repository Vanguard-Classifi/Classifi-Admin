package com.khalidtouch.classifiadmin.model.classifi

import java.time.LocalDateTime


data class ClassifiAcademicSession(
    val sessionId: Long,
    val sessionName: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val schoolId: Long,
)

