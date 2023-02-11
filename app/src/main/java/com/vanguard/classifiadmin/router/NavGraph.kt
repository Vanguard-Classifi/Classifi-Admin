package com.vanguard.classifiadmin.router

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vanguard.classifiadmin.ui.screens.assessments.ASSESSMENT_SCREEN
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentsScreen
import com.vanguard.classifiadmin.ui.screens.calendar.CALENDAR_SCREEN
import com.vanguard.classifiadmin.ui.screens.calendar.MyCalendarScreen
import com.vanguard.classifiadmin.ui.screens.dashboard.MAIN_DASHBOARD_SCREEN
import com.vanguard.classifiadmin.ui.screens.dashboard.MainDashboardScreen
import com.vanguard.classifiadmin.ui.screens.feeds.FEEDS_SCREEN
import com.vanguard.classifiadmin.ui.screens.feeds.FeedsScreen
import com.vanguard.classifiadmin.ui.screens.profile.ACCOUNT_SCREEN
import com.vanguard.classifiadmin.ui.screens.profile.MyAccountScreen
import com.vanguard.classifiadmin.ui.screens.reports.REPORTS_SCREEN
import com.vanguard.classifiadmin.ui.screens.reports.ReportsScreen
import com.vanguard.classifiadmin.ui.screens.results.STUDENT_RESULTS_SCREEN
import com.vanguard.classifiadmin.ui.screens.results.StudentResultsScreen
import com.vanguard.classifiadmin.ui.screens.students.STUDENTS_SCREEN
import com.vanguard.classifiadmin.ui.screens.students.StudentsScreen
import com.vanguard.classifiadmin.ui.screens.support.SUPPORT_SCREEN
import com.vanguard.classifiadmin.ui.screens.support.SupportScreen
import com.vanguard.classifiadmin.viewmodel.MainViewModel

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
                goToFeature = { navController.navigate(it.screen) }
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
    }

}


@Composable
fun BottomNavGraph(
    modifier: Modifier = Modifier,
    startDestination: String = BottomDestination.feeds,
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel,
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
            )
        }

        //assessments
        composable(BottomDestination.assessments) {
            AssessmentsScreen(
                modifier = modifier,
                viewModel = viewModel,
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


object Destinations {
    const val dashboard = MAIN_DASHBOARD_SCREEN
    const val studentResults = STUDENT_RESULTS_SCREEN
    const val support = SUPPORT_SCREEN
    const val calendar = CALENDAR_SCREEN
    const val account = ACCOUNT_SCREEN
}

object BottomDestination {
    const val feeds = FEEDS_SCREEN
    const val students = STUDENTS_SCREEN
    const val assessments = ASSESSMENT_SCREEN
    const val reports = REPORTS_SCREEN
}