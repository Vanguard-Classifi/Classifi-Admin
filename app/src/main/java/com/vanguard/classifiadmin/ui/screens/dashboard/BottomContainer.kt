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
import com.vanguard.classifiadmin.data.local.models.AssessmentModel
import com.vanguard.classifiadmin.data.local.models.FeedModel
import com.vanguard.classifiadmin.data.local.models.UserModel
import com.vanguard.classifiadmin.router.BottomNavGraph
import com.vanguard.classifiadmin.viewmodel.MainViewModel

@Composable
fun BottomContainer(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavHostController = rememberNavController(),
    onStudentOptions: (UserModel) -> Unit,
    onStudentDetail: (UserModel) -> Unit,
    onPublishedAssessmentOptions: (AssessmentModel) -> Unit,
    onInReviewAssessmentOptions: (AssessmentModel) -> Unit,
    onDraftAssessmentOptions: (AssessmentModel) -> Unit,
    onSelectAssessment: (AssessmentModel) -> Unit,
    onSelectClasses: () -> Unit,
    onFeedDetail: (FeedModel) -> Unit,
    onCreateQuestions: () -> Unit,
    onViewReport: (FeedModel) -> Unit,
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
        onSelectClasses = onSelectClasses,
        onFeedDetail = onFeedDetail,
        onStudentDetail = onStudentDetail,
        onCreateQuestions = onCreateQuestions,
        onViewReport = onViewReport
    )
}