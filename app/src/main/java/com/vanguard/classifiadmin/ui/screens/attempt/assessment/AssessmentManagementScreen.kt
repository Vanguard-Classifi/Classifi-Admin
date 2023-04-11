package com.vanguard.classifiadmin.ui.screens.attempt.assessment

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.ui.components.LoadingScreen
import com.vanguard.classifiadmin.ui.components.NoDataScreen
import com.vanguard.classifiadmin.ui.screens.attempt.questions.AssessmentQuestion
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
    val TAG = "AssessmentManagementScreen"
    val isAssessmentComplete by viewModel.isAssessmentComplete.collectAsState()
    val takeAssessmentData by viewModel.takeAssessmentData.collectAsState()
    val isNextQuestionEnabled by viewModel.isNextQuestionEnabled.collectAsState()
    val providedAnswer by viewModel.providedAnswer.collectAsState()

    LaunchedEffect(Unit, takeAssessmentData.data) {
        //init take assessment data
        viewModel.initAssessment()
        Log.e(TAG, "AssessmentManagementScreen: init the assessment data")
    }


    Log.e(TAG, "AssessmentManagementScreen: take assessment data is now ${takeAssessmentData.data}")

    Surface(modifier = Modifier) {
        BoxWithConstraints(modifier = Modifier) {
            val maxHeight = maxHeight
            val maxWidth = maxWidth
            if (isAssessmentComplete) {
                resultContent()
            } else {
                when (takeAssessmentData) {
                    is Resource.Loading -> {
                        viewModel.initAssessment()
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingScreen(maxHeight = maxHeight)
                        }
                    }

                    is Resource.Success -> {
                        if (takeAssessmentData.data != null) {
                            assessmentContent(
                                takeAssessmentData = takeAssessmentData.data!!,
                                isNextEnabled = isNextQuestionEnabled,
                                onNextPressed = { viewModel.onNextPressed() },
                                onPreviousPressed = { viewModel.onPreviousPressed() },
                                onDonePressed = { viewModel.onDonePressed() }
                            ) { paddingValues ->
                                val localModifier = Modifier.padding(paddingValues)

                                AnimatedContent(
                                    targetState = takeAssessmentData,
                                    transitionSpec = {
                                        val animationSpec: TweenSpec<IntOffset> =
                                            tween(300)
                                        val direction = getTransitionDirection(
                                            initialIndex = initialState.data?.questionIndex ?: 0,
                                            targetIndex = initialState.data?.questionIndex ?: 0,
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

                                    AssessmentQuestion(
                                        takeAssessmentData = targetState.data!!,
                                        onOptionsSelected = {
                                            viewModel.onProvideAnswer(it)
                                        },
                                        providedAnswer = providedAnswer.orEmpty(),
                                        onProvideAnswer = {
                                            viewModel.onProvideAnswer(it)
                                        },
                                        minHeight = maxHeight.times(0.5f),
                                        modifier = localModifier
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                NoDataScreen(
                                    maxHeight = maxHeight,
                                    message = stringResource(id = R.string.could_not_load_assessment),
                                    buttonLabel = stringResource(id = R.string.retry),
                                    onClick = {
                                        viewModel.initAssessment()
                                    }
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            NoDataScreen(
                                maxHeight = maxHeight,
                                message = stringResource(id = R.string.could_not_load_assessment),
                                buttonLabel = stringResource(id = R.string.retry),
                                onClick = {
                                    viewModel.initAssessment()
                                }
                            )
                        }
                    }
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
    return if (targetIndex > initialIndex) {
        AnimatedContentScope.SlideDirection.Left
    } else {
        AnimatedContentScope.SlideDirection.Right
    }
}