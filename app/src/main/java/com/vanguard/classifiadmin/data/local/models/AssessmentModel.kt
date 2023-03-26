package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.AssessmentNetworkModel

data class AssessmentModel(
    val assessmentId: String,
    var name: String? = null,
    var subjectId: String? = null,
    var subjectName: String? = null,
    var schoolId: String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var authorId: String? = null,
    var authorName: String? = null,
    var parentFeedId: String? = null,
    var type: String? = null,
    var state: String? = null,
    var assignedClasses: ArrayList<String> = arrayListOf(),
    var assignedStudents: ArrayList<String> = arrayListOf(),
    var questionIds: ArrayList<String> = arrayListOf(),
    var attempts: ArrayList<String> = arrayListOf(),
    var lastModified: String? = null,
    var verified: Boolean? = null,
) {
    fun toNetwork() = AssessmentNetworkModel(
        assessmentId = assessmentId,
        name = name,
        subjectId = subjectId,
        schoolId = schoolId,
        startTime = startTime,
        endTime = endTime,
        startDate = startDate,
        endDate = endDate,
        authorId = authorId,
        authorName = authorName,
        parentFeedId = parentFeedId,
        type = type,
        state = state,
        assignedClasses = assignedClasses,
        assignedStudents = assignedStudents,
        questionIds = questionIds,
        lastModified = lastModified,
        verified = verified,
    )
}


