package com.vanguard.classifiadmin.ui.screens.attempt.assessment

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.buildSpannedString
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.QuestionModel
import com.vanguard.classifiadmin.domain.extensions.supportWideScreen
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.PrimaryTextButtonFillWidth
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.components.SecondaryTextButton
import com.vanguard.classifiadmin.ui.components.SecondaryTextButtonFillWidth
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel


@Composable
fun TakeAssessmentScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onClosePressed: () -> Unit,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    val TAG = "TakeAssessmentScreen"
    val isNextQuestionEnabled by viewModel.isNextQuestionEnabled.collectAsState()
    val takeAssessmentData by viewModel.takeAssessmentData.collectAsState()

    Log.e(TAG, "TakeAssessmentScreen: the take assessment screen is called")
    Log.e(
        TAG,
        "TakeAssessmentScreen: current assessment data index is ${takeAssessmentData.data?.questionIndex}"
    )


    Surface(modifier = Modifier.supportWideScreen()) {
        Scaffold(
            topBar = {
                TakeAssessmentScreenTopBar(
                    onClosePressed = onClosePressed,
                    questionCount = takeAssessmentData.data?.questionCount ?: 0,
                    questionIndex = takeAssessmentData.data?.questionIndex ?: 0
                )
            },
            content = content,
            bottomBar = {
                TakeAssessmentScreenBottomBar(
                    shouldShowPreviousButton = takeAssessmentData.data?.shouldShowPreviousButton ?: false,
                    shouldShowDoneButton = takeAssessmentData.data?.shouldShowDoneButton ?: false,
                    isNextButtonEnabled = isNextQuestionEnabled,
                    onPreviousPressed = onPreviousPressed,
                    onNextPressed = onNextPressed,
                    onDonePressed = onDonePressed
                )
            }
        )
    }
}


@Composable
fun TakeAssessmentScreenTopBar(
    modifier: Modifier = Modifier,
    onClosePressed: () -> Unit,
    questionIndex: Int,
    questionCount: Int,
) {
    val TAG = "TakeAssessmentScreenTopBar"

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "34:23",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100.copy(0.8f)
                )

                RoundedIconButton(
                    onClick = onClosePressed,
                    icon = R.drawable.icon_close
                )
            }

            Text(
                text = "${questionIndex + 1} of $questionCount",
                fontSize = 12.sp,
                color = Black100.copy(0.8f)
            )
        }

        Spacer(modifier = Modifier.height(22.dp))
        val animatedProgress by animateFloatAsState(
            targetValue = (questionIndex + 1) / questionCount.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )

        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colors.primary,
            backgroundColor = Black100.copy(0.1f)
        )
        Spacer(modifier = Modifier.height(16.dp))
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
    val TAG = "TakeAssessmentScreenBottomBar"

    Surface(modifier = Modifier.fillMaxWidth(), elevation = 7.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (shouldShowPreviousButton) {
                SecondaryTextButtonFillWidth(
                    label = stringResource(id = R.string.previous),
                    onClick = onPreviousPressed,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(4.dp))
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
            questionIndex = 0,
            questionCount = 0,
            shouldShowPreviousButton = false,
            shouldShowDoneButton = false,
            currentQuestion = QuestionModel("<Unknown>")
        )
    }
}