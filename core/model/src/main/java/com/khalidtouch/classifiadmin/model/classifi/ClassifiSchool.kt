package com.khalidtouch.classifiadmin.model.classifi

import java.time.LocalDateTime

data class ClassifiSchool(
    val schoolId: Long,
    val schoolName: String,
    val address: String,
    val description: String,
    val bannerImage: String,
    val dateCreated: LocalDateTime,
    val students: List<ClassifiUser>,
    val teachers: List<ClassifiUser>,
    val parents: List<ClassifiUser>,
    val classes: List<ClassifiClass>,
    val sessions: List<ClassifiAcademicSession>,
) {
    val currentSession: ClassifiAcademicSession?
        get() = if (sessions.isNotEmpty()) sessions.last() else null
}