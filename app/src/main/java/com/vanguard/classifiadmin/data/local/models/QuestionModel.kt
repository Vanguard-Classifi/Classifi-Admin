package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.QuestionNetworkModel

data class QuestionModel(
    val questionId: String,
    var type: String? = null,
    var schoolId: String? = null,
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
    fun toNetwork() = QuestionNetworkModel(
        questionId = questionId,
        type = type,
        schoolId = schoolId,
        difficulty = difficulty,
        maxScore = maxScore,
        text = text,
        optionA = optionA,
        optionB = optionB,
        optionC = optionC,
        optionD = optionD,
        answers = answers,
        authorId = authorId,
        parentAssessmentIds = parentAssessmentIds,
        essayWordLimit = essayWordLimit,
        topicId = topicId,
        topicName = topicName,
        verified = verified,
        position = position,
        lastModified = lastModified,
    )

    companion object {
        val Default = QuestionModel(
            questionId = "<Unknown>",
        )
    }
}