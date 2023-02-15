package com.vanguard.classifiadmin.ui.screens.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vanguard.classifiadmin.router.BottomNavGraph
import com.vanguard.classifiadmin.ui.screens.assessments.Assessment
import com.vanguard.classifiadmin.viewmodel.MainViewModel

@Composable
fun BottomContainer(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavHostController = rememberNavController(),
    onStudentOptions: () -> Unit,
    onPublishedAssessmentOptions: (Assessment) -> Unit,
    onInReviewAssessmentOptions: (Assessment) -> Unit,
    onDraftAssessmentOptions: (Assessment) -> Unit,
    onSelectAssessment: (Assessment) -> Unit,
) {
    BottomNavGraph(
        viewModel = viewModel,
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        onStudentOptions = onStudentOptions,
        onSelectAssessment = onSelectAssessment,
        onPublishedAssessmentOptions = onPublishedAssessmentOptions,
        onInReviewAssessmentOptions = onInReviewAssessmentOptions,
        onDraftAssessmentOptions = onDraftAssessmentOptions,
    )
}