package com.vanguard.classifiadmin.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.khalidtouch.classifiadmin.feeds.navigation.feedsNavigationRoute
import com.khalidtouch.classifiadmin.feeds.navigation.navigateToFeeds
import com.khalidtouch.classifiadmin.students.navigation.navigateToStudents
import com.khalidtouch.classifiadmin.students.navigation.studentsNavigationRoute
import com.vanguard.classifiadmin.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberClassifiAppState(
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberAnimatedNavController(),
    bottomNavController: NavHostController = rememberAnimatedNavController(),
): ClassifiAppState {
    //todo: navigation tracking side effect
    return remember(navController, coroutineScope, windowSizeClass) {
        ClassifiAppState(bottomNavController, navController, coroutineScope, windowSizeClass)
    }
}


@Stable
class ClassifiAppState(
    val bottomNavController: NavHostController,
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    private val windowSizeClass: WindowSizeClass
) {
    val currentDestination: NavDestination?
        @Composable get() = bottomNavController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            feedsNavigationRoute -> TopLevelDestination.Feeds
            studentsNavigationRoute -> TopLevelDestination.Students
            else -> TopLevelDestination.Feeds
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowSettingsTab: Boolean
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
                popUpTo(bottomNavController.graph.findStartDestination().id) {
                    saveState = true
                }

                launchSingleTop = true
                restoreState = true
            }

            when (topLevelDestination) {
                TopLevelDestination.Feeds -> bottomNavController.navigateToFeeds(topLevelNavOptions)
                TopLevelDestination.Students -> bottomNavController.navigateToStudents(
                    topLevelNavOptions
                )
            }

        }
    }
}