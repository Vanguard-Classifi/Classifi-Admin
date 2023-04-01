package com.vanguard.classifiadmin.data.network.models

import com.vanguard.classifiadmin.data.local.models.QuestionModel

data class QuestionNetworkModel(
    var questionId: String? = null,
    var schoolId: String? = null,
    var type: String? = null,
    var difficulty: String? = null,
    var maxScore: Int? = null,
    var text: String? = null,
    var optionA: String? = null,
    var optionB: String? = null,
    var optionC: String? = null,
    var optionD: String? = null,
    var parentAssessmentIds: ArrayList<String> = arrayListOf(),
    var answers: ArrayList<String> = arrayListOf(),
    var authorId: String? = null,
    var essayWordLimit: Int? = null,
    var topicId: String? = null,
    var topicName: String? = null,
    var verified: Boolean? = null,
    var position: Int? = null,
    var lastModified: String? = null,
) {
    fun toLocal() = QuestionModel(
        questionId = questionId.orEmpty(),
        type = type,
        difficulty = difficulty,
        schoolId = schoolId,
        maxScore = maxScore,
        text = text,
        authorId = authorId,
        optionA = optionA,
        optionB = optionB,
        optionC = optionC,
        optionD = optionD,
        answers = answers,
        parentAssessmentIds = parentAssessmentIds,
        essayWordLimit = essayWordLimit,
        topicId = topicId,
        topicName = topicName,
        verified = verified,
        position = position,
        lastModified = lastModified,
    )
}