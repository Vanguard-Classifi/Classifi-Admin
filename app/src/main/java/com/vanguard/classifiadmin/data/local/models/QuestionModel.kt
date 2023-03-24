package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.QuestionNetworkModel

data class QuestionModel(
    val questionId: String,
    var parentAssessmentId: String? = null,
    var type: String? = null,
    var difficulty: String? = null,
    var maxScore: Int? = null,
    var duration: String? = null,
    var text: String? = null,
    var optionA: String? = null,
    var optionB: String? = null,
    var optionC: String? = null,
    var optionD: String? = null,
    var answers: ArrayList<String> = arrayListOf(),
    var essayWordLimit: Int? = null,
    var topicId: String? = null,
    var topicName: String? = null,
) {
    fun toNetwork() = QuestionNetworkModel(
        questionId = questionId,
        parentAssessmentId = parentAssessmentId,
        type = type,
        difficulty = difficulty,
        maxScore = maxScore,
        duration = duration,
        text = text,
        optionA = optionA,
        optionB = optionB,
        optionC = optionC,
        optionD = optionD,
        answers = answers,
        essayWordLimit = essayWordLimit,
        topicId = topicId,
        topicName = topicName,
    )
}