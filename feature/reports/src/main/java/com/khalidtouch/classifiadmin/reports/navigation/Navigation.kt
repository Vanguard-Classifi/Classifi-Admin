package com.khalidtouch.classifiadmin.reports.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val reportsNavigationRoute = "reports_navigation_route"

fun NavController.navigateToReports(navOptions: NavOptions? = null) {
    this.navigate(reportsNavigationRoute, navOptions)
}

fun NavGraphBuilder.reportsScreen() {
    composable(route = reportsNavigationRoute) {
       ReportsRoute()
    }
}
