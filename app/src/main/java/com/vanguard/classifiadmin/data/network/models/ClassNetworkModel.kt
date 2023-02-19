package com.vanguard.classifiadmin.data.network.models

import com.vanguard.classifiadmin.data.local.models.ClassModel


data class ClassNetworkModel(
    val classId: String? = null,
    var className: String? = null,
    var studentIds: ArrayList<String> = arrayListOf(),
    var teacherIds: ArrayList<String> = arrayListOf(),
    var subjectIds: ArrayList<String> = arrayListOf(),
    var dateCreated: String? = null,
    var modifiedBy: String? = null,
    var lastModified: String? = null,
) {
    fun toLocal() = ClassModel(
        classId = classId ?: "",
        className = className,
        studentIds = studentIds,
        teacherIds = teacherIds,
        subjectIds = subjectIds,
        dateCreated = dateCreated,
        modifiedBy = modifiedBy,
        lastModified = lastModified,
    )
}