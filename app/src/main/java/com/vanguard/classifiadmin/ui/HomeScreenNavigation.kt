package com.vanguard.classifiadmin.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable

const val homeScreenNavigationRoute = "homeScreenNavigationRoute"




fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeScreenNavigationRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeScreen(
    windowSizeClass: WindowSizeClass,
    appState: ClassifiAppState,
    onComposeFeed: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    composable(
        route = homeScreenNavigationRoute,
    ) {
        ClassifiHomeScreen(
            windowSizeClass = windowSizeClass,
            appState = appState,
            onComposeFeed = onComposeFeed,
            onOpenSettings = onOpenSettings,
        )
    }
}