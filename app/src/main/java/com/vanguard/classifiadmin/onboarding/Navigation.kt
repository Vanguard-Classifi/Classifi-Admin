package com.vanguard.classifiadmin.onboarding

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.navigation.animation.AnimatedNavHost


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingNavHost(
    loginRequiredOnly: Boolean,
    onboardingUiState: OnboardingUiState,
    loginUiState: LoginUiState,
    navigateToLoginScreen: () -> Unit,
    navigateToCreateAccountScreen: () -> Unit,
    onEmailChangedWhileLoggingIn: (String) -> Unit,
    onPasswordChangedWhileLoggingIn: (String) -> Unit,
    createAccountUiState: CreateAccountUiState,
    onEmailChangedWhileCreatingAccount: (String) -> Unit,
    onPasswordChangedWhileCreatingAccount: (String) -> Unit,
    onConfirmPasswordChangedWhileCreatingAccount: (String) -> Unit,
) {
    LaunchedEffect(loginRequiredOnly) {
        if (loginRequiredOnly) navigateToLoginScreen()
    }

    AnimatedNavHost(
        navController = (onboardingUiState as OnboardingUiState.Success).data.navController,
        startDestination = if (loginRequiredOnly) loginScreenNavigationRoute else welcomeScreenNavigationRoute
    ) {
        //onboarding screen
        welcomeScreen()
        //create account screen
        createAccountScreen(
            onLogin = navigateToLoginScreen,
            createAccountUiState = createAccountUiState,
            onEmailChanged = onEmailChangedWhileCreatingAccount,
            onPasswordChanged = onPasswordChangedWhileCreatingAccount,
            onConfirmPasswordChanged = onConfirmPasswordChangedWhileCreatingAccount,
        )

        loginScreen(
            onForgotPassword = {/*TODO: Go to forgot password */ },
            onCreateAccount = navigateToCreateAccountScreen,
            loginRequiredOnly = loginRequiredOnly,
            loginUiState = loginUiState,
            onEmailChanged = onEmailChangedWhileLoggingIn,
            onPasswordChanged = onPasswordChangedWhileLoggingIn,
        )

        onboardingSuccess()
    }
}

