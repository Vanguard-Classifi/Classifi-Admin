package com.khalidtouch.classifiadmin.settings.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.khalidtouch.classifiadmin.settings.navigation.settings.SettingsRoute

const val settingsNavigationRoute = "settingsNavigationRoute"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(settingsNavigationRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsScreen(
    windowSizeClass: WindowSizeClass,
    onBack: () -> Unit,
) {
    composable(
        route = settingsNavigationRoute,
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
        SettingsRoute(
            windowSizeClass = windowSizeClass,
            onBack = onBack,
        )
    }
}