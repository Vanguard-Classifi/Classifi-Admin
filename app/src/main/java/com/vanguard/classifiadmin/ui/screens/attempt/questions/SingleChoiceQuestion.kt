package com.vanguard.classifiadmin.ui.screens.attempt.questions

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionType
import com.vanguard.classifiadmin.ui.screens.attempt.assessment.TakeAssessmentData
import com.vanguard.classifiadmin.ui.theme.Black100

@Composable
fun SingleChoiceQuestion(
    modifier: Modifier = Modifier,
    selectedAnswer: String,
    takeAssessmentData: TakeAssessmentData,
    onOptionSelected: (String) -> Unit
) {
    val options = takeAssessmentData.currentQuestion.let {
        arrayListOf(it.optionA, it.optionB, it.optionC, it.optionD)
    }
    val TAG = "SingleChoiceQuestion"


    QuestionWrapper(
        title = takeAssessmentData.currentQuestion.text.orEmpty(),
        directionsRes = R.string.choose_the_correct_answer,
        modifier = modifier.selectableGroup(),
    ) {
        options.forEach { option ->
            val selected = option.orEmpty() == selectedAnswer
            OptionWithRadioButton(
                modifier = modifier.padding(vertical = 8.dp),
                text = option.orEmpty(),
                selected = selected,
                onOptionSelected = onOptionSelected
            )
        }
    }
}


@Composable
fun OptionWithRadioButton(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onOptionSelected: (String) -> Unit,
) {
    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(
                onClick = { onOptionSelected(text) },
                role = Role.RadioButton
            ),
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            if (selected) MaterialTheme.colors.primary else Black100.copy(0.5f)
        ),
        color = if (selected) MaterialTheme.colors.primary.copy(0.3f) else
            Color.Transparent
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = text,
                modifier = modifier.weight(1f),
                fontWeight = FontWeight.Medium,
                color = Black100.copy(0.8f)
            )
            Spacer(modifier = modifier.width(8.dp))

            Box(modifier = modifier.padding(8.dp)){
                RadioButton(
                    selected,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colors.primary,
                    )
                )
            }
        }
    }
}