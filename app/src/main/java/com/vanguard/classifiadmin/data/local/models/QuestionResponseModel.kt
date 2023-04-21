package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.QuestionResponseNetworkModel


data class QuestionResponseModel(
    val responseId: String,
    var type: String? = null,
    var schoolId: String? = null,
    var difficulty: String? = null,
    var text: String? = null,
    var optionA: String? = null,
    var optionB: String? = null,
    var optionC: String? = null,
    var optionD: String? = null,
    var actualAnswer: String? = null,
    var providedAnswer: String? = null,
    var parentAssessmentId: String? = null,
    var studentId: String? = null,
    var position: Int? = null,
    var lastModified: String? = null,
) {
    fun toLocal() = QuestionResponseNetworkModel(
        responseId = responseId,
        type = type,
        schoolId = schoolId,
        difficulty = difficulty,
        text = text,
        optionA = optionA,
        optionB = optionB,
        optionC = optionC,
        optionD = optionD,
        actualAnswer = actualAnswer,
        providedAnswer = providedAnswer,
        parentAssessmentId = parentAssessmentId,
        studentId = studentId,
        position = position,
        lastModified = lastModified
    )
}