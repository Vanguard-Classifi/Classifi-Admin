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
        onNextPressed: () -> Unit,
        onPreviousPressed: () -> Unit,
        onDonePressed: () -> Unit,
        questionContent: @Composable (PaddingValues) -> Unit,
    ) -> Unit = { onNextPressed, onPreviousPressed, onDonePressed, questionContent ->
        TakeAssessmentScreen(
            viewModel = viewModel,
            onNextPressed = onNextPressed,
            onPreviousPressed = onPreviousPressed,
            onDonePressed = onDonePressed,
            onClosePressed = onBack,
            content = questionContent
        )
    },
) {
    val TAG = "AssManagementScreen"
    val isAssessmentComplete by viewModel.isAssessmentComplete.collectAsState()
    val takeAssessmentData by viewModel.takeAssessmentData.collectAsState()
    val isNextQuestionEnabled by viewModel.isNextQuestionEnabled.collectAsState()
    val providedAnswer by viewModel.providedAnswer.collectAsState()

    LaunchedEffect(Unit) {
        //init take assessment data
        viewModel.initAssessment()
        Log.e(TAG, "AssessmentManagementScreen: init the assessment data")
    }

    Log.e(TAG, "AssessmentManagementScreen: AssessmentManagementScreen has been called")
    Log.e(TAG, "AssessmentManagementScreen: take assessment data is now ${takeAssessmentData.data}")

    Surface(modifier = Modifier) {
        BoxWithConstraints(modifier = Modifier) {

        }
    }
}

