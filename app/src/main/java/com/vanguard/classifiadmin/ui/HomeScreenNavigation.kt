package com.vanguard.classifiadmin.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable

const val homeScreenNavigationRoute = "homeScreenNavigationRoute"


fun NavController.navigateToHome() {
    this.navigate(homeScreenNavigationRoute) {
        launchSingleTop = true
        popUpTo(homeScreenNavigationRoute)
    }
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
        enterTransition = {
            fadeIn()
        },
        exitTransition = {
            fadeOut()
        }
    ) {
        ClassifiHomeScreen(
            windowSizeClass = windowSizeClass,
            appState = appState,
            onComposeFeed = onComposeFeed,
            onOpenSettings = onOpenSettings,
        )
    }
}