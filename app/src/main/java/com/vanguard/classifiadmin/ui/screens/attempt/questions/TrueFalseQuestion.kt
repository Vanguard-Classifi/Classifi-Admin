package com.vanguard.classifiadmin.ui.screens.attempt.questions

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionOptionTrueFalse
import com.vanguard.classifiadmin.ui.screens.attempt.assessment.TakeAssessmentData

@Composable
fun TrueFalseQuestion(
    modifier: Modifier = Modifier,
    takeAssessmentData: TakeAssessmentData,
    onOptionSelected: (String) -> Unit,
    selectedAnswer: String,
) {
    QuestionWrapper(
        title = takeAssessmentData.currentQuestion.text.orEmpty(),
        directionsRes = R.string.select_your_answer,
        modifier = modifier.selectableGroup()
    ) {
        QuestionOptionTrueFalse.values().forEach { option ->
            val selected = selectedAnswer == option.name
            OptionWithRadioButton(
                modifier = Modifier.padding(vertical = 8.dp),
                text = option.name,
                selected = selected,
                onOptionSelected = onOptionSelected
            )
        }
    }
}

