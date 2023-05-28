package com.vanguard.classifiadmin.onboarding

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingNavHost(
    windowSizeClass: WindowSizeClass,
    uiState: OnboardingUiState,
) {
    AnimatedNavHost(
        navController = (uiState as OnboardingUiState.Success).data.navController,
        startDestination = welcomeScreenNavigationRoute
    ) {
        //onboarding screen
        welcomeScreen()
        //create account screen
        createAccountScreen()
    }
}

