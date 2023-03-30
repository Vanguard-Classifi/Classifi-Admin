package com.vanguard.classifiadmin.ui.screens.assessments.questions.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionOptionTrueFalse

@Composable
fun TrueFalseItem(
    modifier: Modifier = Modifier,
    answer: String,
    options: List<QuestionOptionTrueFalse> = QuestionOptionTrueFalse.values().toList(),
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        options.forEach { option ->
            QuestionOptionItem(
                content = option.name,
                option = if (option == QuestionOptionTrueFalse.True)
                    "A" else "B",
                selected = option.name == answer,
            )
            Spacer(modifier = modifier.width(8.dp))
        }
    }
}