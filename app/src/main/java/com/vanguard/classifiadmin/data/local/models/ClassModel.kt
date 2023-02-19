package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.ClassNetworkModel

data class ClassModel(
    val classId: String,
    var className: String? = null,
    var studentIds: ArrayList<String> = arrayListOf(),
    var teacherIds: ArrayList<String> = arrayListOf(),
    var subjectIds: ArrayList<String> = arrayListOf(),
    var dateCreated: String? = null,
    var modifiedBy: String? = null,
    var lastModified: String? = null,
) {
    fun toNetwork() = ClassNetworkModel(
        classId = classId,
        className = className,
        studentIds = studentIds,
        teacherIds = teacherIds,
        subjectIds = subjectIds,
        dateCreated = dateCreated,
        modifiedBy = modifiedBy,
        lastModified = lastModified,
    )
}