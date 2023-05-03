package com.vanguard.classifiadmin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.khalidtouch.classifiadmin.assessment.navigation.assessmentsScreen
import com.khalidtouch.classifiadmin.feeds.navigation.feedsNavigationRoute
import com.khalidtouch.classifiadmin.feeds.navigation.feedsScreen
import com.khalidtouch.classifiadmin.reports.navigation.reportsScreen
import com.khalidtouch.classifiadmin.students.navigation.studentsScreen

@Composable
fun ClassifiNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = feedsNavigationRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        feedsScreen()
        studentsScreen()
        assessmentsScreen()
        reportsScreen()
    }
}