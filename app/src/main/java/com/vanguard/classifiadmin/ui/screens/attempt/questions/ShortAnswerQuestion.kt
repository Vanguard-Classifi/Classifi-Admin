package com.vanguard.classifiadmin.ui.screens.attempt.questions

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.screens.attempt.assessment.TakeAssessmentData

@Composable
fun ShortAnswerQuestion(
    modifier: Modifier = Modifier,
    minHeight: Dp,
    providedAnswer: String,
    onProvideAnswer: (String) -> Unit,
    takeAssessmentData: TakeAssessmentData,
) {
    QuestionWrapper(
        title = takeAssessmentData.currentQuestion.text.orEmpty(),
        directionsRes = R.string.provide_short_answer,
        modifier = modifier.selectableGroup()
    ){
        OutlinedTextField(
            value = providedAnswer,
            onValueChange = onProvideAnswer,
            maxLines = 5,
            modifier = Modifier.heightIn(min = minHeight).padding(vertical = 16.dp),
            shape = MaterialTheme.shapes.small,
        )
    }
}