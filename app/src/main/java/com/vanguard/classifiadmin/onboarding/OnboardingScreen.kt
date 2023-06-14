package com.vanguard.classifiadmin.onboarding

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khalidtouch.classifiadmin.model.utils.CreateAccountData
import com.khalidtouch.classifiadmin.model.utils.OnCreateAccountState
import com.khalidtouch.core.designsystem.components.ClassifiTextButton
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.onboarding.usecase.LoginData
import com.vanguard.classifiadmin.onboarding.usecase.OnLoginState


@Composable
fun OnboardingDialog(
    windowSizeClass: WindowSizeClass,
    loginRequiredOnly: Boolean,
    onboardingUiState: OnboardingUiState = rememberOnboardingUiState(),
    onboardingViewModel: OnboardingViewModel = hiltViewModel<OnboardingViewModel>(),
    createAccountViewModel: CreateAccountViewModel = hiltViewModel<CreateAccountViewModel>(),
    loginViewModel: LoginViewModel = hiltViewModel<LoginViewModel>()
) {
    val TAG = "Onboarding"
    val navController = (onboardingUiState as OnboardingUiState.Success).data.navController
    val onboardingState by onboardingViewModel.state.collectAsStateWithLifecycle()
    val createAccountUiState by createAccountViewModel.createAccountUiState.collectAsStateWithLifecycle()
    val loginUiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()


    OnboardingDialog(
        currentOnboardingDestination = onboardingState.currentDestination,
        navigateToCreateAccount = {
            navController.navigateToCreateAccount(
                updateCurrentOnboardingDestination = onboardingViewModel::updateCurrentDestination
            )
        },
        navigateToOnboardingSuccessfulScreen = {
            navController.navigateToOnboardingSuccessfulScreen(
                updateCurrentOnboardingDestination = onboardingViewModel::updateCurrentDestination
            )
        },
        onLogin = loginViewModel::onLogin,
        onFinishOnboarding = onboardingViewModel::finishOnboarding,
        onCreateSuperUser = createAccountViewModel::createSuperUser,
        onEmailChangedWhileLoggingIn = loginViewModel::onEmailChanged,
        onPasswordChangedWhileLoggingIn = loginViewModel::onPasswordChanged,
        createAccountUiState = createAccountUiState,
        loginUiState = loginUiState,
        onboardingUiState = onboardingUiState,
        navigateToLoginScreen = {
            navController.navigateToLoginScreen(
                updateCurrentOnboardingDestination = onboardingViewModel::updateCurrentDestination
            )
        },
        onEmailChangedWhileCreatingAccount = createAccountViewModel::onEmailChanged,
        onPasswordChangedWhileCreatingAccount = createAccountViewModel::onPasswordChanged,
        onConfirmPasswordChangedWhileCreatingAccount = createAccountViewModel::onConfirmPasswordChanged,
        loginRequiredOnly = loginRequiredOnly,
    )
}

@Composable
private fun OnboardingDialog(
    loginRequiredOnly: Boolean,
    currentOnboardingDestination: OnboardingDestination,
    navigateToCreateAccount: () -> Unit,
    navigateToOnboardingSuccessfulScreen: () -> Unit,
    onLogin: (LoginData, callback: (OnLoginState) -> Unit) -> Unit,
    onFinishOnboarding: () -> Unit,
    onCreateSuperUser: (data: CreateAccountData, callback: (OnCreateAccountState) -> Unit) -> Unit,
    onEmailChangedWhileLoggingIn: (String) -> Unit,
    onPasswordChangedWhileLoggingIn: (String) -> Unit,
    createAccountUiState: CreateAccountUiState,
    onEmailChangedWhileCreatingAccount: (String) -> Unit,
    onPasswordChangedWhileCreatingAccount: (String) -> Unit,
    onConfirmPasswordChangedWhileCreatingAccount: (String) -> Unit,
    onboardingUiState: OnboardingUiState,
    loginUiState: LoginUiState,
    navigateToLoginScreen: () -> Unit,
) {
    val TAG = "Onboarding"
    val configuration = LocalConfiguration.current

    AlertDialog(
        onDismissRequest = {
        },
        confirmButton = {
            Box(Modifier.padding(horizontal = 8.dp)) {
                ClassifiTextButton(
                    onClick = {
                        when (currentOnboardingDestination) {
                            OnboardingDestination.WELCOME -> {
                                navigateToCreateAccount()
                            }

                            OnboardingDestination.CREATE_ACCOUNT -> {
                                when (createAccountUiState) {
                                    is CreateAccountUiState.Loading -> Unit
                                    is CreateAccountUiState.Success -> {
                                        Log.e(TAG, "OnboardingScreen: create account")
                                        onCreateSuperUser(
                                            CreateAccountData(
                                                email = (createAccountUiState as CreateAccountUiState.Success).data.email,
                                                password = (createAccountUiState as CreateAccountUiState.Success).data.password,
                                                confirmPassword = (createAccountUiState as CreateAccountUiState.Success).data.confirm,
                                            )
                                        ) { onCreateAccountState ->
                                            when (onCreateAccountState) {
                                                OnCreateAccountState.Starting -> {
                                                    //todo start loading bar
                                                }

                                                OnCreateAccountState.Success -> {
                                                    navigateToOnboardingSuccessfulScreen()
                                                }

                                                OnCreateAccountState.Failed -> {
                                                    Log.e(TAG, "OnboardingScreen: failed ")
                                                }

                                                OnCreateAccountState.EmptyEmailOrPassword -> {
                                                    Log.e(
                                                        TAG,
                                                        "OnboardingScreen: empty email or password "
                                                    )
                                                }

                                                OnCreateAccountState.PasswordNotMatched -> {
                                                    Log.e(
                                                        TAG,
                                                        "OnboardingScreen: password not matched "
                                                    )
                                                }

                                                OnCreateAccountState.InvalidCredentials -> {
                                                    Log.e(
                                                        TAG,
                                                        "OnboardingScreen: invalid credentials "
                                                    )
                                                }

                                                OnCreateAccountState.InvalidUser -> {
                                                    Log.e(TAG, "OnboardingScreen: invalid user ")
                                                }

                                                OnCreateAccountState.UserAlreadyExists -> {
                                                    Log.e(
                                                        TAG,
                                                        "OnboardingScreen: user already exists "
                                                    )
                                                }

                                                OnCreateAccountState.WeakPassword -> {
                                                    Log.e(TAG, "OnboardingScreen: weak password ")
                                                }

                                                OnCreateAccountState.EmailNotFound -> {
                                                    Log.e(TAG, "OnboardingScreen: email not found ")
                                                }

                                                OnCreateAccountState.NetworkProblem -> {
                                                    Log.e(TAG, "OnboardingScreen: network issue ")
                                                }

                                                else -> Unit
                                            }
                                        }
                                    }
                                }
                            }

                            OnboardingDestination.LOGIN -> {
                                when (loginUiState) {
                                    is LoginUiState.Loading -> Unit
                                    is LoginUiState.Success -> {
                                        onLogin(
                                            LoginData(
                                                (loginUiState as LoginUiState.Success).data.email,
                                                (loginUiState as LoginUiState.Success).data.password,
                                            )
                                        ) { loginState ->
                                            when (loginState) {
                                                is OnLoginState.Success -> {
                                                    Log.e(TAG, "OnboardingScreen: success")
                                                    onFinishOnboarding()
                                                }

                                                is OnLoginState.Failed -> {
                                                    Log.e(TAG, "OnboardingScreen: failed")
                                                }

                                                is OnLoginState.NetworkProblem -> {
                                                    Log.e(TAG, "OnboardingScreen: network issues")
                                                }

                                                is OnLoginState.WeakPassword -> {
                                                    Log.e(TAG, "OnboardingScreen: weak password")
                                                }

                                                is OnLoginState.UserAlreadyExists -> {
                                                    Log.e(
                                                        TAG,
                                                        "OnboardingScreen: user already exists"
                                                    )
                                                }

                                                is OnLoginState.InvalidUser -> {
                                                    Log.e(TAG, "OnboardingScreen: invalid user")
                                                }

                                                is OnLoginState.InvalidCredentials -> {
                                                    Log.e(
                                                        TAG,
                                                        "OnboardingScreen: invalid credentials"
                                                    )
                                                }

                                                is OnLoginState.EmailNotFound -> {
                                                    Log.e(TAG, "OnboardingScreen: email not found")
                                                }

                                                is OnLoginState.EmptyEmailOrPassword -> {
                                                    Log.e(
                                                        TAG,
                                                        "OnboardingScreen: empty email or password"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            OnboardingDestination.SUCCESS -> {
                                onFinishOnboarding()
                            }
                        }
                    },
                    text = {
                        val text = when (currentOnboardingDestination) {
                            OnboardingDestination.SUCCESS -> stringResource(id = R.string.finish)
                            else -> {
                                if (loginRequiredOnly) {
                                    stringResource(id = R.string.finish)
                                } else stringResource(id = R.string.next)
                            }
                        }
                        Text(
                            text = text,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                )
            }
        },
        dismissButton = {},
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            OnboardingNavHost(
                onboardingUiState = onboardingUiState,
                loginRequiredOnly = loginRequiredOnly,
                loginUiState = loginUiState,
                createAccountUiState = createAccountUiState,
                navigateToLoginScreen = navigateToLoginScreen,
                navigateToCreateAccountScreen = navigateToCreateAccount,
                onEmailChangedWhileCreatingAccount = onEmailChangedWhileCreatingAccount,
                onPasswordChangedWhileCreatingAccount = onPasswordChangedWhileCreatingAccount,
                onConfirmPasswordChangedWhileCreatingAccount = onConfirmPasswordChangedWhileCreatingAccount,
                onEmailChangedWhileLoggingIn = onEmailChangedWhileLoggingIn,
                onPasswordChangedWhileLoggingIn = onPasswordChangedWhileLoggingIn,
            )
        },
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(16.dp),
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
        ),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 16.dp),
    )

}