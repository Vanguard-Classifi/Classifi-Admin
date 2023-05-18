package com.khalidtouch.classifiadmin.model.classifi

import java.time.LocalDateTime


data class ClassifiClass(
    val classId: Long,
    val schoolId: Long,
    val className: String,
    val classCode: String,
    val formTeacherId: Long,
    val prefectId: Long,
    val dateCreated: LocalDateTime,
    val creatorId: Long,
    val feeds: List<ClassifiFeed>,
    val teachers: List<ClassifiUser>,
    val students: List<ClassifiUser>,
)