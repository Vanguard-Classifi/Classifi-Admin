package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.ClassNetworkModel

data class ClassModel(
    val classId: String,
    var className: String? = null,
    var classCode: String? = null,
    var schoolId: String? = null,
    var schoolName: String? = null,
    var studentIds: ArrayList<String> = arrayListOf(),
    var teacherIds: ArrayList<String> = arrayListOf(),
    var subjectIds: ArrayList<String> = arrayListOf(),
    var verified: Boolean = false,
    var dateCreated: String? = null,
    var modifiedBy: String? = null,
    var lastModified: String? = null,
) {
    fun toNetwork() = ClassNetworkModel(
        classId = classId,
        className = className,
        classCode = classCode,
        schoolId = schoolId,
        schoolName = schoolName,
        studentIds = studentIds,
        teacherIds = teacherIds,
        subjectIds = subjectIds,
        verified = verified,
        dateCreated = dateCreated,
        modifiedBy = modifiedBy,
        lastModified = lastModified,
    )
}