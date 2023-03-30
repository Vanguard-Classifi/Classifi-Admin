package com.vanguard.classifiadmin.ui.screens.assessments.questions.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionDifficulty
import com.vanguard.classifiadmin.ui.screens.assessments.questions.items.QuestionDifficultyItem
import com.vanguard.classifiadmin.viewmodel.MainViewModel


@Composable
fun QuestionDifficultyBottomSheetContent(
    modifier: Modifier = Modifier,
    difficulties: List<QuestionDifficulty> = QuestionDifficulty.values().toList(),
    onSelect: (QuestionDifficulty) -> Unit,
    onClose: () -> Unit,
    viewModel: MainViewModel,
) {
    Surface(modifier = modifier) {
        Column(modifier = modifier) {
            difficulties.forEach { difficulty ->
                QuestionDifficultyItem(difficulty = difficulty, onSelect = {
                    viewModel.onQuestionDifficultyChanged(it)
                    onSelect(it)
                    onClose()
                })
            }
        }
    }
}
