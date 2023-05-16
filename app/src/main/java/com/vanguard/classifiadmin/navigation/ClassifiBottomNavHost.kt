package com.vanguard.classifiadmin.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.khalidtouch.classifiadmin.feeds.navigation.feedsNavigationRoute
import com.khalidtouch.classifiadmin.feeds.navigation.feedsScreen
import com.khalidtouch.classifiadmin.students.navigation.studentsScreen


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ClassifiBottomNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = feedsNavigationRoute,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        feedsScreen()
        studentsScreen()
    }
}