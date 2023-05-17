package com.vanguard.classifiadmin.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.khalidtouch.classifiadmin.settings.navigation.navigateToSettings
import com.khalidtouch.classifiadmin.settings.navigation.settingsScreen
import com.vanguard.classifiadmin.ui.ClassifiAppState
import com.vanguard.classifiadmin.ui.homeScreen
import com.vanguard.classifiadmin.ui.homeScreenNavigationRoute
import com.vanguard.classifiadmin.ui.navigateToHome


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
        homeScreen(
            windowSizeClass = windowSizeClass,
            onOpenSettings = { appState.navController.navigateToSettings() })
        settingsScreen(
            windowSizeClass = windowSizeClass, onBack = { appState.navController.navigateToHome() })
    }
}