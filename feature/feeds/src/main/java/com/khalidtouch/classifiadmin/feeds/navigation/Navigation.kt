package com.khalidtouch.classifiadmin.feeds.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.khalidtouch.classifiadmin.feeds.FeedsRoute

const val feedsNavigationRoute = "feeds_navigation_route"

fun NavController.navigateToFeeds(navOptions: NavOptions? = null) {
    this.navigate(feedsNavigationRoute, navOptions)
}

fun NavGraphBuilder.feedsScreen() {
    composable(route = feedsNavigationRoute) {
        FeedsRoute()
    }
}
