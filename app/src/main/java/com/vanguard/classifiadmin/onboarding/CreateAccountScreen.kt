package com.vanguard.classifiadmin.onboarding

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable

@Composable
fun CreateAccountScreen() {
    Text("create account screen")
}

const val createAccountNavigationRoute = "create_account_navigation_route"

fun NavController.navigateToCreateAccount(
    onboardingViewModel: OnboardingViewModel,
    navOptions: NavOptions? = null
) {
    this.navigate(createAccountNavigationRoute, navOptions)
    onboardingViewModel.updateCurrentDestination(OnboardingDestination.CREATE_ACCOUNT)
}


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.createAccountScreen() {
    composable(
        route = createAccountNavigationRoute,
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
        CreateAccountScreen()
    }
}