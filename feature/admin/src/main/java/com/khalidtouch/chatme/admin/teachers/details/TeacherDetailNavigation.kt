package com.khalidtouch.chatme.admin.teachers.details

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.khalidtouch.chatme.admin.teachers.TeacherScreenViewModel

const val teacherDetailNavigationRoute = "teacher_detail_navigation_route"


fun NavController.navigateToTeacherDetail(navOptions: NavOptions? = null) {
    this.navigate(teacherDetailNavigationRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.teacherDetailScreen(
    teacherDetailViewModel: TeacherDetailViewModel,
    teacherScreenViewModel: TeacherScreenViewModel,
    onBackPressed: () -> Unit,
) {
    composable(
        route = teacherDetailNavigationRoute,
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
        TeacherDetailScreen(
            teacherDetailViewModel = teacherDetailViewModel,
            teacherScreenViewModel = teacherScreenViewModel,
            onBackPressed = onBackPressed,
        )
    }
}