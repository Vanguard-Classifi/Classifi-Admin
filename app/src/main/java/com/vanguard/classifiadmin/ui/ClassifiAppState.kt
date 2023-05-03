package com.vanguard.classifiadmin.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.khalidtouch.classifiadmin.assessment.navigation.assessmentsNavigationRoute
import com.khalidtouch.classifiadmin.assessment.navigation.navigateToAssessments
import com.khalidtouch.classifiadmin.feeds.navigation.feedsNavigationRoute
import com.khalidtouch.classifiadmin.feeds.navigation.navigateToFeeds
import com.khalidtouch.classifiadmin.reports.navigation.navigateToReports
import com.khalidtouch.classifiadmin.reports.navigation.reportsNavigationRoute
import com.khalidtouch.classifiadmin.students.navigation.navigateToStudents
import com.khalidtouch.classifiadmin.students.navigation.studentsNavigationRoute
import com.vanguard.classifiadmin.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberClassifiAppState(
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
): ClassifiAppState {
    //todo: navigation tracking side effect
    return remember(navController, coroutineScope, windowSizeClass) {
        ClassifiAppState(navController, coroutineScope, windowSizeClass)
    }
}


@Stable
class ClassifiAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    /*TODO -> currentTopLevelDestination */
    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            feedsNavigationRoute -> TopLevelDestination.Feeds
            studentsNavigationRoute -> TopLevelDestination.Students
            assessmentsNavigationRoute -> TopLevelDestination.Assessments
            reportsNavigationRoute -> TopLevelDestination.Reports
            else -> TopLevelDestination.Feeds
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    var shouldShowSettingsDialog by mutableStateOf(false)
        private set

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    fun setShowSettingsDialog(shouldShow: Boolean) {
        shouldShowSettingsDialog = shouldShow
    }

    fun navigationToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }

                launchSingleTop = true
                restoreState = true
            }

            when(topLevelDestination) {
                TopLevelDestination.Feeds -> navController.navigateToFeeds(topLevelNavOptions)
                TopLevelDestination.Students -> navController.navigateToStudents(topLevelNavOptions)
                TopLevelDestination.Assessments -> navController.navigateToAssessments(topLevelNavOptions)
                TopLevelDestination.Reports -> navController.navigateToReports(topLevelNavOptions)
            }

        }
    }
}