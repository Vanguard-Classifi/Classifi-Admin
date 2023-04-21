package com.vanguard.classifiadmin.data.file

import com.vanguard.classifiadmin.data.local.models.QuestionModel
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionType


object DummyQuestions {
    val questions = listOf<QuestionModel>(
        QuestionModel(
            questionId = "question1",
            type = QuestionType.TrueFalse.title,
            text = "Allah is the King",
            optionA = "True",
            optionB = "False",
            position = 1
        ),
        QuestionModel(
            questionId = "question2",
            type = QuestionType.TrueFalse.title,
            text = "Allah is the Most Gracious",
            optionA = "True",
            optionB = "False",
            position = 2
        ),
        QuestionModel(
            questionId = "question3",
            type = QuestionType.MultiChoice.title,
            text = "What is Bismillahi",
            optionA = "In the name of Allah",
            optionB = "The true one",
            optionC = "Thy shalt not eat",
            optionD = "The oneness of Allah",
            position = 3
        ),
        QuestionModel(
            questionId = "question4",
            type = QuestionType.MultiChoice.title,
            text = "What is a house",
            optionA = "Where you live",
            optionB = "A place to hunt",
            optionC = "A tree",
            optionD = "A bush",
            position = 4
        ),
        QuestionModel(
            questionId = "question5",
            type = QuestionType.MultiChoice.title,
            text = "What is a fan",
            optionA = "A home equipment that cools us off",
            optionB = "A heater",
            optionC = "A light emitter",
            optionD = "The brick lace of a wall",
            position = 5
        ),
    )

    fun getQuestionByPosition(position: Int): QuestionModel {
        return questions.find { it.position == position } ?: QuestionModel.Default
    }
}