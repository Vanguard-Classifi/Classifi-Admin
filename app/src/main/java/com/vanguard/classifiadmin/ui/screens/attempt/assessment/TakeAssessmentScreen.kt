package com.vanguard.classifiadmin.ui.screens.attempt.assessment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.QuestionModel
import com.vanguard.classifiadmin.domain.extensions.supportWideScreen
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.PrimaryTextButtonFillWidth
import com.vanguard.classifiadmin.ui.components.SecondaryTextButton

@Composable
fun TakeAssessmentScreen(
    modifier: Modifier = Modifier,
    takeAssessmentData: TakeAssessmentData,
    isNextEnabled: Boolean,
    onClosePressed: () -> Unit,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Surface(modifier = Modifier.supportWideScreen()) {
        Scaffold(
            topBar = {},
            content = content,
            bottomBar = {
                TakeAssessmentScreenBottomBar(
                    shouldShowPreviousButton = takeAssessmentData.shouldShowPreviousButton,
                    shouldShowDoneButton = takeAssessmentData.shouldShowDoneButton,
                    isNextButtonEnabled = isNextEnabled,
                    onPreviousPressed = onPreviousPressed,
                    onNextPressed = onNextPressed,
                    onDonePressed = onDonePressed
                )
            }
        )
    }
}


@Composable
fun TakeAssessmentScreenBottomBar(
    modifier: Modifier = Modifier,
    shouldShowPreviousButton: Boolean,
    shouldShowDoneButton: Boolean,
    isNextButtonEnabled: Boolean,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit,
) {
    Surface(modifier = Modifier.fillMaxWidth(), elevation = 7.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (shouldShowPreviousButton) {
                SecondaryTextButton(
                    label = stringResource(id = R.string.previous),
                    onClick = onPreviousPressed,
                    modifier = Modifier.weight(1f),
                )
            }
            if (shouldShowDoneButton) {
                PrimaryTextButtonFillWidth(
                    modifier = Modifier.weight(1f),
                    label = stringResource(id = R.string.done),
                    onClick = onDonePressed,
                    enabled = isNextButtonEnabled,
                )
            } else {
                PrimaryTextButtonFillWidth(
                    modifier = Modifier.weight(1f),
                    label = stringResource(id = R.string.next),
                    onClick = onNextPressed,
                    enabled = isNextButtonEnabled,
                )
            }
        }
    }
}


data class TakeAssessmentData(
    val questionIndex: Int,
    val questionCount: Int,
    val shouldShowPreviousButton: Boolean,
    val shouldShowDoneButton: Boolean,
    val currentQuestion: QuestionModel,
) {
    companion object {
        val Default = TakeAssessmentData(
            questionIndex = -1,
            questionCount = 0,
            shouldShowPreviousButton = false,
            shouldShowDoneButton = false,
            currentQuestion = QuestionModel("<Unknown>")
        )
    }
}