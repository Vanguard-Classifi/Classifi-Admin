package com.khalidtouch.chatme.admin.school.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.khalidtouch.chatme.admin.school.SchoolAppState
import com.khalidtouch.chatme.admin.school.rememberSchoolAppState
import com.khalidtouch.chatme.admin.school.schoolNavigationRoute
import com.khalidtouch.chatme.admin.school.schoolScreen
import com.khalidtouch.core.designsystem.components.ClassifiBackground
import com.khalidtouch.core.designsystem.components.ClassifiGradientBackground
import com.khalidtouch.core.designsystem.theme.LocalGradientColors

const val schoolAdminPanelNavigationRoute = "school_admin_panel_navigation_route"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.schoolAdminPanel(
    windowSizeClass: WindowSizeClass,
    onBackPressed: () -> Unit,
) {
    composable(
        route = schoolAdminPanelNavigationRoute,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(200)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(200)
            )
        }
    ) {
        SchoolAdminPanel(windowSizeClass = windowSizeClass, onBackPressed = onBackPressed)
    }
}


@Composable
private fun SchoolAdminPanel(
    windowSizeClass: WindowSizeClass,
    onBackPressed: () -> Unit,
) {
    ClassifiBackground {
        ClassifiGradientBackground(
            gradientColors = LocalGradientColors.current
        ) {
            Column(Modifier.fillMaxSize()) {
                SchoolNavHost(windowSizeClass = windowSizeClass, onBackPressed = onBackPressed)
            }
        }
    }
}

fun NavController.navigateToSchoolAdminPanel(navOptions: NavOptions? = null) {
    this.navigate(schoolAdminPanelNavigationRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SchoolNavHost(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    appState: SchoolAppState = rememberSchoolAppState(windowSizeClass = windowSizeClass),
    startDestination: String = schoolNavigationRoute,
    onBackPressed: () -> Unit,
) {
    AnimatedNavHost(
        navController = appState.navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        schoolScreen(
            onBackPressed = onBackPressed,
            windowSizeClass = windowSizeClass,
        )
    }
}