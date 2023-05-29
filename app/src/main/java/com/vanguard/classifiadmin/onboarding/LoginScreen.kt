package com.vanguard.classifiadmin.onboarding

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.onboarding.usecase.OnLoginState

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onCreateAccount: () -> Unit,
    onForgotPassword: () -> Unit,
) {
    val TAG = "LoginScreen"
    val context = LocalContext.current
    var isPasswordVisible by remember { mutableStateOf(false) }
    val uiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()

    Log.e(TAG, "LoginScreen: email is ${(uiState as LoginUiState.Success).data.email} ")
    Log.e(TAG, "LoginScreen: password is ${(uiState as LoginUiState.Success).data.password}")

    LazyColumn {
        when (uiState) {
            is LoginUiState.Loading -> Unit
            is LoginUiState.Success -> {
                item {
                    Box {
                        Text(
                            text = stringResource(id = R.string.welcome_back),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            )
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                }

                emailItem(
                    value = (uiState as LoginUiState.Success).data.email,
                    onValueChange = loginViewModel::onEmailChanged,
                    placeholder = context.getString(R.string.enter_your_email)
                )

                passwordItem(
                    value = (uiState as LoginUiState.Success).data.password,
                    onValueChange = loginViewModel::onPasswordChanged,
                    placeholder = context.getString(R.string.password),
                    isPasswordVisible = isPasswordVisible,
                    onTogglePasswordVisibility = {
                        isPasswordVisible = !it
                    }
                )

                forgotPasswordItem(onForgotPassword)
                createAccountInstead(onCreateAccount)
            }
        }
    }

    AnimatedVisibility(
        visible = uiState is LoginUiState.Loading,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
        ) + fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            ClassifiLoadingWheel()
        }
    }
}


fun LazyListScope.createAccountInstead(
    onCreateAccount: () -> Unit,
) {
    item {
        Row {
            Text(
                text = stringResource(id = R.string.create_account_instead),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier.clickable(
                    enabled = true,
                    onClick = onCreateAccount,
                    role = Role.Button,
                )
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

fun LazyListScope.forgotPasswordItem(
    onForgotPassword: () -> Unit,
) {
    item {
        Row {
            Text(
                text = stringResource(id = R.string.forgot_password),
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.click_here),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier.clickable(
                    enabled = true,
                    onClick = onForgotPassword,
                    role = Role.Button,
                )
            )
        }
        Spacer(Modifier.height(16.dp))

    }
}

const val loginScreenNavigationRoute = "login_screen_navigation_route"

fun NavController.navigateToLoginScreen(
    onboardingViewModel: OnboardingViewModel,
    navOptions: NavOptions? = null
) {
    this.navigate(loginScreenNavigationRoute, navOptions)
    onboardingViewModel.updateCurrentDestination(OnboardingDestination.LOGIN)
}


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.loginScreen(
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
    loginViewModel: LoginViewModel,
) {
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
        LoginScreen(
            onForgotPassword = onForgotPassword,
            onCreateAccount = onCreateAccount,
            loginViewModel = loginViewModel
        )
    }
}