package com.vanguard.classifiadmin.onboarding

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable

@Composable
fun LoginScreen() {
}

const val loginScreenNavigationRoute = "login_screen_navigation_route"

fun NavController.navigateToLoginScreen(
    onboardingViewModel: OnboardingViewModel,
    navOptions: NavOptions? = null
) {
    this.navigate(loginScreenNavigationRoute, navOptions)
}


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.loginScreen() {
    composable(
        route = loginScreenNavigationRoute,
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
        LoginScreen()
    }
}