package com.vanguard.classifiadmin.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.khalidtouch.classifiadmin.feeds.navigation.feedsNavigationRoute
import com.khalidtouch.classifiadmin.feeds.navigation.feedsScreen
import com.khalidtouch.classifiadmin.students.navigation.studentsScreen
import com.vanguard.classifiadmin.ui.ClassifiAppState
import com.vanguard.classifiadmin.ui.homeScreen
import com.vanguard.classifiadmin.ui.homeScreenNavigationRoute


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ClassifiNavHost(
    windowSizeClass: WindowSizeClass,
    appState: ClassifiAppState,
    modifier: Modifier = Modifier,
    startDestination: String = homeScreenNavigationRoute,
) {
    AnimatedNavHost(
        navController = appState.navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
       homeScreen(windowSizeClass = windowSizeClass)
    }
}