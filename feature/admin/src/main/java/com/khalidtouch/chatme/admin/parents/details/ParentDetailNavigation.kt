package com.khalidtouch.chatme.admin.parents.details

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.khalidtouch.chatme.admin.parents.ParentScreenViewModel

const val parentDetailNavigationRoute = "parent_detail_navigation_route"


fun NavController.navigateToParentDetail(navOptions: NavOptions? = null) {
    this.navigate(parentDetailNavigationRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.parentDetailScreen(
    parentDetailViewModel: ParentDetailViewModel,
    parentScreenViewModel: ParentScreenViewModel,
    onBackPressed: () -> Unit,
) {
    composable(
        route = parentDetailNavigationRoute,
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
        ParentDetailScreen(
            parentDetailViewModel = parentDetailViewModel,
            parentScreenViewModel = parentScreenViewModel,
            onBackPressed = onBackPressed,
        )
    }
}