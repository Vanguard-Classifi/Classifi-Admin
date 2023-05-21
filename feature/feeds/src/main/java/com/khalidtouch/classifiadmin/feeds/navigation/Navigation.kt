package com.khalidtouch.classifiadmin.feeds.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.khalidtouch.classifiadmin.feeds.FeedsRoute
import com.google.accompanist.navigation.animation.composable

const val feedsNavigationRoute = "feeds_navigation_route"

fun NavController.navigateToFeeds(navOptions: NavOptions? = null) {
    this.navigate(feedsNavigationRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.feedsScreen(
    onComposeFeed: () -> Unit,
) {
    composable(
        route = feedsNavigationRoute,
    ) {
        FeedsRoute(onComposeFeed = onComposeFeed)
    }
}
