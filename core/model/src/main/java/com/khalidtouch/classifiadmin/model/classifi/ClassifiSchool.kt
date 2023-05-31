package com.khalidtouch.classifiadmin.model.classifi

import java.time.LocalDateTime

data class ClassifiSchool(
    val schoolId: Long? = null,
    var schoolName: String? = null,
    var address: String? = null,
    var description: String? = null,
    var bannerImage: String? = null,
    var dateCreated: LocalDateTime? = null,
    var students: List<ClassifiUser> = emptyList(),
    var teachers: List<ClassifiUser> = emptyList(),
    var parents: List<ClassifiUser> = emptyList(),
    var admins: List<ClassifiUser> = emptyList(),
    var classes: List<ClassifiClass> = emptyList(),
    var sessions: List<ClassifiAcademicSession> = emptyList(),
) {
    val currentSession: ClassifiAcademicSession?
        get() = if (sessions.isNotEmpty()) sessions.last() else null
}