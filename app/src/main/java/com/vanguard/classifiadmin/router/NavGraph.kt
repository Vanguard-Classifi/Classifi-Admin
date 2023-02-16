package com.vanguard.classifiadmin.router

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vanguard.classifiadmin.ui.screens.assessments.ASSESSMENT_REPORT_SCREEN
import com.vanguard.classifiadmin.ui.screens.assessments.ASSESSMENT_REVIEW_SCREEN
import com.vanguard.classifiadmin.ui.screens.assessments.ASSESSMENT_SCREEN
import com.vanguard.classifiadmin.ui.screens.assessments.Assessment
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentReportScreen
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentReviewScreen
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentsScreen
import com.vanguard.classifiadmin.ui.screens.assessments.MODIFY_ASSESSMENT_SCREEN
import com.vanguard.classifiadmin.ui.screens.assessments.ModifyAssessmentScreen
import com.vanguard.classifiadmin.ui.screens.calendar.CALENDAR_SCREEN
import com.vanguard.classifiadmin.ui.screens.calendar.MyCalendarScreen
import com.vanguard.classifiadmin.ui.screens.classes.MANAGE_CLASS_SCREEN
import com.vanguard.classifiadmin.ui.screens.classes.ManageClassScreen
import com.vanguard.classifiadmin.ui.screens.dashboard.MAIN_DASHBOARD_SCREEN
import com.vanguard.classifiadmin.ui.screens.dashboard.MainDashboardScreen
import com.vanguard.classifiadmin.ui.screens.feeds.FEEDS_SCREEN
import com.vanguard.classifiadmin.ui.screens.feeds.FeedsScreen
import com.vanguard.classifiadmin.ui.screens.profile.ACCOUNT_SCREEN
import com.vanguard.classifiadmin.ui.screens.profile.MyAccountScreen
import com.vanguard.classifiadmin.ui.screens.profile.STUDENT_PROFILE_SCREEN
import com.vanguard.classifiadmin.ui.screens.profile.StudentProfileScreen
import com.vanguard.classifiadmin.ui.screens.reports.REPORTS_SCREEN
import com.vanguard.classifiadmin.ui.screens.reports.ReportsScreen
import com.vanguard.classifiadmin.ui.screens.results.STUDENT_RESULTS_SCREEN
import com.vanguard.classifiadmin.ui.screens.results.StudentResultsScreen
import com.vanguard.classifiadmin.ui.screens.students.STUDENTS_SCREEN
import com.vanguard.classifiadmin.ui.screens.students.StudentsScreen
import com.vanguard.classifiadmin.ui.screens.support.SUPPORT_SCREEN
import com.vanguard.classifiadmin.ui.screens.support.SupportScreen
import com.vanguard.classifiadmin.viewmodel.MainViewModel

object Destinations {
    const val dashboard = MAIN_DASHBOARD_SCREEN
    const val studentResults = STUDENT_RESULTS_SCREEN
    const val support = SUPPORT_SCREEN
    const val calendar = CALENDAR_SCREEN
    const val account = ACCOUNT_SCREEN
    const val manageClass = MANAGE_CLASS_SCREEN
    const val studentProfile = STUDENT_PROFILE_SCREEN
    const val assessmentReport = ASSESSMENT_REPORT_SCREEN
    const val assessmentReview = ASSESSMENT_REVIEW_SCREEN
    const val modifyAssessment = MODIFY_ASSESSMENT_SCREEN
}


@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    finishActivity: () -> Unit,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destinations.dashboard,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Destinations.dashboard) {
            MainDashboardScreen(
                viewModel = viewModel,
                goToFeature = { navController.navigate(it.screen) },
                onSelectMenu = { navController.navigate(it.screen) },
                onSelectProfile = { navController.navigate(Destinations.account) },
                onManageClass = { navController.navigate(Destinations.manageClass) },
                goToAssessmentReport = { navController.navigate(Destinations.assessmentReport) },
                goToAssessmentReview = { navController.navigate(Destinations.assessmentReview) },
                goToModifyAssessment = { navController.navigate(Destinations.modifyAssessment) }
            )
        }

        composable(Destinations.studentResults) {
            StudentResultsScreen(
                viewModel = viewModel
            )
        }

        composable(Destinations.support) {
            SupportScreen()
        }

        composable(Destinations.calendar) {
            MyCalendarScreen()
        }

        composable(Destinations.account) {
            MyAccountScreen()
        }

        composable(Destinations.manageClass) {
            ManageClassScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.dashboard) }
            )
        }

        composable(Destinations.studentProfile) {
            StudentProfileScreen(
                viewModel = viewModel,
            )
        }

        composable(Destinations.assessmentReport) {
            AssessmentReportScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.dashboard) },
            )
        }

        composable(Destinations.assessmentReview) {
            AssessmentReviewScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.dashboard) }
            )
        }

        composable(Destinations.modifyAssessment) {
            ModifyAssessmentScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.dashboard) }
            )
        }
    }
}


object BottomDestination {
    const val feeds = FEEDS_SCREEN
    const val students = STUDENTS_SCREEN
    const val assessments = ASSESSMENT_SCREEN
    const val reports = REPORTS_SCREEN
}

@Composable
fun BottomNavGraph(
    modifier: Modifier = Modifier,
    startDestination: String = BottomDestination.feeds,
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel,
    onStudentOptions: () -> Unit,
    onPublishedAssessmentOptions: (Assessment) -> Unit,
    onInReviewAssessmentOptions: (Assessment) -> Unit,
    onDraftAssessmentOptions: (Assessment) -> Unit,
    onSelectAssessment: (Assessment) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        //feeds
        composable(BottomDestination.feeds) {
            FeedsScreen(
                modifier = modifier,
                viewModel = viewModel,
            )
        }

        //students
        composable(BottomDestination.students) {
            StudentsScreen(
                modifier = modifier,
                viewModel = viewModel,
                onStudentOptions = onStudentOptions,
            )
        }

        //assessments
        composable(BottomDestination.assessments) {
            AssessmentsScreen(
                modifier = modifier,
                viewModel = viewModel,
                onSelectAssessment = onSelectAssessment,
                onPublishedAssessmentOptions = onPublishedAssessmentOptions,
                onInReviewAssessmentOptions = onInReviewAssessmentOptions,
                onDraftAssessmentOptions = onDraftAssessmentOptions,
            )
        }

        //reports
        composable(BottomDestination.reports) {
            ReportsScreen(
                modifier = modifier,
                viewModel = viewModel,
            )
        }
    }
}