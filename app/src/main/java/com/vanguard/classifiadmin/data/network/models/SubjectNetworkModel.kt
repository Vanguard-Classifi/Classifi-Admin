package com.vanguard.classifiadmin.data.network.models

import com.vanguard.classifiadmin.data.local.models.SubjectModel

data class SubjectNetworkModel(
    val subjectId: String? = null,
    var subjectName: String? = null,
    var subjectCode: String? = null,
    var schoolId: String? = null,
    var classId: String? = null,
    var className: String? = null,
    var verified: Boolean = false,
    var teacherIds: ArrayList<String> = arrayListOf(),
    var studentIds: ArrayList<String> = arrayListOf(),
    var lastModified: String? = null,
) {
    fun toLocal() = SubjectModel(
        subjectId = subjectId ?: "",
        subjectName = subjectName,
        subjectCode = subjectCode,
        schoolId = schoolId,
        classId = classId,
        className = className,
        verified = verified,
        teacherIds = teacherIds,
        studentIds = studentIds,
        lastModified = lastModified
    )
}