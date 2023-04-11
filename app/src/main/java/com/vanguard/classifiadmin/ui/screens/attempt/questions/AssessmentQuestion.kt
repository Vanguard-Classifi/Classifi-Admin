package com.vanguard.classifiadmin.ui.screens.attempt.questions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionType
import com.vanguard.classifiadmin.ui.screens.attempt.assessment.TakeAssessmentData


@Composable
fun AssessmentQuestion(
    modifier: Modifier = Modifier,
    takeAssessmentData: TakeAssessmentData,
    onOptionsSelected: (String) -> Unit,
    providedAnswer: String,
    onProvideAnswer: (String) -> Unit,
    minHeight: Dp,
) {
    when(takeAssessmentData.currentQuestion.type){
        QuestionType.MultiChoice.title -> {
            SingleChoiceQuestion(
                selectedAnswer = providedAnswer,
                takeAssessmentData = takeAssessmentData,
                onOptionSelected = onOptionsSelected
            )
        }
        QuestionType.Essay.title -> {
            EssayQuestion(
                minHeight = minHeight,
                providedAnswer = providedAnswer,
                onProvideAnswer = onProvideAnswer,
                takeAssessmentData = takeAssessmentData
            )
        }
        QuestionType.TrueFalse.title -> {
            TrueFalseQuestion(
                takeAssessmentData = takeAssessmentData,
                onOptionSelected = onOptionsSelected,
                selectedAnswer = providedAnswer
            )
        }
        QuestionType.Short.title -> {
            ShortAnswerQuestion(
                minHeight = minHeight,
                providedAnswer = providedAnswer,
                onProvideAnswer = onProvideAnswer,
                takeAssessmentData = takeAssessmentData
            )
        }
    }
}
