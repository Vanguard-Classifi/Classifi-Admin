package com.vanguard.classifiadmin.ui.screens.assessments.questions.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionType
import com.vanguard.classifiadmin.ui.screens.assessments.questions.items.QuestionTypeItem
import com.vanguard.classifiadmin.viewmodel.MainViewModel


@Composable
fun QuestionTypeBottomSheetContent(
    modifier: Modifier = Modifier,
    questionTypes: List<QuestionType> = QuestionType.values().toList(),
    onSelect: (QuestionType) -> Unit,
    onClose: () -> Unit,
    viewModel: MainViewModel,
) {
    Surface(modifier = modifier) {
        Column(modifier = modifier) {
            questionTypes.forEach { questionType ->
                QuestionTypeItem(type = questionType, onSelect = {
                    viewModel.onQuestionTypeChanged(it)
                    onSelect(it)
                    onClose()
                })
            }
        }
    }
}