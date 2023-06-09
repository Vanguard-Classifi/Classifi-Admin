package com.khalidtouch.chatme.admin.teachers.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.khalidtouch.chatme.admin.teachers.TeacherScreenViewModel
import com.khalidtouch.chatme.admin.teachers.TeachersAppState
import com.khalidtouch.chatme.admin.teachers.TeachersRoute
import com.khalidtouch.chatme.admin.teachers.addteacher.AddTeacherViewModel
import com.khalidtouch.chatme.admin.teachers.details.TeacherDetailViewModel
import com.khalidtouch.chatme.admin.teachers.details.navigateToTeacherDetail
import com.khalidtouch.chatme.admin.teachers.details.teacherDetailScreen
import com.khalidtouch.chatme.admin.teachers.navigateToTeachersScreen
import com.khalidtouch.chatme.admin.teachers.rememberTeachersAppState
import com.khalidtouch.chatme.admin.teachers.teachersScreen
import com.khalidtouch.chatme.admin.teachers.teachersScreenNavigationRoute
import com.khalidtouch.core.designsystem.components.ClassifiBackground
import com.khalidtouch.core.designsystem.components.ClassifiGradientBackground
import com.khalidtouch.core.designsystem.theme.LocalGradientColors

const val teachersAdminPanelNavigationRoute = "teachers_admin_panel_navigation_route"


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.teachersAdminPanel(
    windowSizeClass: WindowSizeClass,
    onBackPressed: () -> Unit,
) {
    composable(
        route = teachersAdminPanelNavigationRoute,
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
        TeachersAdminPanel(
            windowSizeClass = windowSizeClass,
            onBackPressed = onBackPressed,
        )
    }
}


fun NavController.navigateToTeachersAdminPanel(navOptions: NavOptions? = null) {
    this.navigate(teachersAdminPanelNavigationRoute, navOptions)
}


@Composable
private fun TeachersAdminPanel(
    windowSizeClass: WindowSizeClass,
    onBackPressed: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        TeachersNavHost(
            windowSizeClass = windowSizeClass,
            onBackPressed = onBackPressed,
        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun TeachersNavHost(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    appState: TeachersAppState = rememberTeachersAppState(windowSizeClass = windowSizeClass),
    startDestination: String = teachersScreenNavigationRoute,
    onBackPressed: () -> Unit,
    teacherScreenViewModel: TeacherScreenViewModel = hiltViewModel<TeacherScreenViewModel>(),
    addTeacherViewModel: AddTeacherViewModel = hiltViewModel<AddTeacherViewModel>(),
    teacherDetailViewModel: TeacherDetailViewModel = hiltViewModel<TeacherDetailViewModel>()
) {
    AnimatedNavHost(
        navController = appState.navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        teachersScreen(
            onBackPressed = onBackPressed,
            teacherScreenViewModel = teacherScreenViewModel,
            addTeacherViewModel = addTeacherViewModel,
            windowSizeClass = windowSizeClass,
            onOpenTeacherDetail = { appState.navController.navigateToTeacherDetail() }
        )

        teacherDetailScreen(
            teacherScreenViewModel = teacherScreenViewModel,
            teacherDetailViewModel = teacherDetailViewModel,
            onBackPressed = { appState.navController.navigateToTeachersScreen() }
        )
    }
}