package com.khalidtouch.chatme.admin.school.modifyschool

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.khalidtouch.chatme.admin.school.SchoolViewModel

const val modifySchoolNavigationRoute = "modify_school_navigation_route"

fun NavController.navigateToModifySchool(navOptions: NavOptions? = null) {
    this.navigate(modifySchoolNavigationRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.modifySchoolScreen(
    onBackPressed: () -> Unit,
    schoolViewModel: SchoolViewModel,
) {
    composable(
        route = modifySchoolNavigationRoute,
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
        ModifySchoolRoute(
            onBackPressed = onBackPressed,
            schoolViewModel = schoolViewModel,
        )
    }
}

