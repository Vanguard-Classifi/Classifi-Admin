package com.vanguard.classifiadmin.ui.screens.attempt.assessment

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.with
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val ASSESSMENT_MANAGEMENT_SCREEN = "assessment_management_screen"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AssessmentManagementScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    resultContent: @Composable () -> Unit = {},
    assessmentContent: @Composable (
        takeAssessmentData: TakeAssessmentData,
        isNextEnabled: Boolean,
        onNextPressed: () -> Unit,
        onPreviousPressed: () -> Unit,
        onDonePressed: () -> Unit,
        questionContent: @Composable (PaddingValues) -> Unit,
    ) -> Unit = { data, nextEnabled, onNextPressed, onPreviousPressed, onDonePressed, questionContent ->
        TakeAssessmentScreen(
            takeAssessmentData = data,
            isNextEnabled = nextEnabled,
            onNextPressed = onNextPressed,
            onPreviousPressed = onPreviousPressed,
            onDonePressed = onDonePressed,
            onClosePressed = onBack,
            content = questionContent
        )
    },
) {
    val isAssessmentComplete by viewModel.isAssessmentComplete.collectAsState()
    val takeAssessmentData by viewModel.takeAssessmentData.collectAsState()
    val isNextQuestionEnabled by viewModel.isNextQuestionEnabled.collectAsState()

    Surface(modifier = Modifier) {
        if (isAssessmentComplete) {
            resultContent()
        } else {
            assessmentContent(
                takeAssessmentData = takeAssessmentData ?: TakeAssessmentData.Default,
                isNextEnabled = isNextQuestionEnabled,
                onNextPressed = { viewModel.onNextPressed() },
                onPreviousPressed = { viewModel.onPreviousPressed() },
                onDonePressed = { viewModel.onDonePressed() }
            ) { paddingValues ->
                val modifier = Modifier.padding(paddingValues)

                AnimatedContent(
                    targetState = takeAssessmentData,
                    transitionSpec = {
                        val animationSpec: TweenSpec<IntOffset> =
                            tween(300)
                        val direction = getTransitionDirection(
                            initialIndex = initialState?.questionIndex ?: 0,
                            targetIndex = initialState?.questionIndex ?: 0,
                        )
                        slideIntoContainer(
                            towards = direction,
                            animationSpec = animationSpec
                        ) with slideOutOfContainer(
                            towards = direction,
                            animationSpec = animationSpec
                        )

                    },
                ) { targetState ->
                    /*todo: question type goes here*/
                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
private fun getTransitionDirection(
    initialIndex: Int,
    targetIndex: Int
): AnimatedContentScope.SlideDirection {
    return if(targetIndex > initialIndex){
        AnimatedContentScope.SlideDirection.Left
    } else {
        AnimatedContentScope.SlideDirection.Right
    }
}