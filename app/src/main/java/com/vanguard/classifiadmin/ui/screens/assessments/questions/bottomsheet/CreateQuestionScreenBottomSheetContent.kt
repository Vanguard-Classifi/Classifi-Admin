package com.vanguard.classifiadmin.ui.screens.assessments.questions.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanguard.classifiadmin.ui.screens.assessments.questions.CreateQuestionBottomSheetMode
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionDifficulty
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionType
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel

@Composable
fun CreateQuestionScreenBottomSheetContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onSelectQuestionType: (QuestionType) -> Unit,
    onSelectQuestionDifficulty: (QuestionDifficulty) -> Unit,
    onClose: () -> Unit,
) {
    val mode by viewModel.createQuestionBottomSheetMode.collectAsState()

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(16.dp),
                color = Black100.copy(0.5f)
            ) {
                Box(
                    modifier = modifier
                        .width(102.dp)
                        .height(3.dp)
                )
            }
        }

        when (mode) {
            is CreateQuestionBottomSheetMode.QuestionType -> {
                QuestionTypeBottomSheetContent(
                    onSelect = onSelectQuestionType,
                    viewModel = viewModel,
                    onClose = onClose
                )
            }

            is CreateQuestionBottomSheetMode.Difficulty -> {
                QuestionDifficultyBottomSheetContent(
                    onSelect = onSelectQuestionDifficulty,
                    onClose = onClose,
                    viewModel = viewModel,
                )
            }

            is CreateQuestionBottomSheetMode.Score -> {
                //score screen
                QuestionScoreBottomSheetContent(
                    viewModel = viewModel,
                    onClose = onClose,
                )
            }

            is CreateQuestionBottomSheetMode.Duration -> {
                QuestionDurationBottomSheetContent(
                    viewModel = viewModel,
                    onClose = onClose,
                )
            }
        }

    }
}
