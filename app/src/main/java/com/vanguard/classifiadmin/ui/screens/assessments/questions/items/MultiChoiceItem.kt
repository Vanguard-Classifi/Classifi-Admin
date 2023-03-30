package com.vanguard.classifiadmin.ui.screens.assessments.questions.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionOption

@Composable
fun MultiChoiceItem(
    modifier: Modifier = Modifier,
    answer: String,
    contents: List<String> = emptyList(),
    options: List<QuestionOption> = QuestionOption.values().toList(),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        options.forEachIndexed { index, option ->
            QuestionOptionItem(
                content = contents[index],
                option = when (option) {
                    QuestionOption.OptionA -> "A"
                    QuestionOption.OptionB -> "B"
                    QuestionOption.OptionC -> "C"
                    QuestionOption.OptionD -> "D"
                },
                selected = contents[index] == answer,
            )
        }
    }
}