package com.vanguard.classifiadmin.onboarding

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingNavHost(
    onboardingViewModel: OnboardingViewModel,
    createAccountViewModel: CreateAccountViewModel,
    loginViewModel: LoginViewModel,
    loginRequiredOnly: Boolean,
    windowSizeClass: WindowSizeClass,
    uiState: OnboardingUiState,
) {
    AnimatedNavHost(
        navController = (uiState as OnboardingUiState.Success).data.navController,
        startDestination = if(loginRequiredOnly) loginScreenNavigationRoute else  welcomeScreenNavigationRoute
    ) {
        //onboarding screen
        welcomeScreen()
        //create account screen
        createAccountScreen(
            createAccountViewModel = createAccountViewModel,
            onLogin = { uiState.data.navController.navigateToLoginScreen(onboardingViewModel) }
        )

        loginScreen(
            onForgotPassword = {/*TODO: Go to forgot password */},
            onCreateAccount = {uiState.data.navController.navigateToCreateAccount(onboardingViewModel)},
            loginViewModel = loginViewModel,
            loginRequiredOnly = loginRequiredOnly,
        )

        onboardingSuccess()
    }
}

