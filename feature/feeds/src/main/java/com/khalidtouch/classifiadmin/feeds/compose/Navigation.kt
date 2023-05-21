package com.khalidtouch.classifiadmin.feeds.compose

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable

const val composeFeedNavigationRoute = "compose_feed_navigation_route"

fun NavController.navigateToComposeFeed(navOptions: NavOptions? = null) {
    this.navigate(composeFeedNavigationRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.composeFeedScreen(
    onCloseComposeFeedScreen: () -> Unit,
) {
    composable(
        route = composeFeedNavigationRoute,
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
        ComposeFeedRoute(onCloseComposeFeedScreen = onCloseComposeFeedScreen)
    }
}