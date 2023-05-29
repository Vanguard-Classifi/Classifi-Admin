package com.vanguard.classifiadmin.onboarding

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khalidtouch.core.designsystem.components.ClassifiTextButton
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.onboarding.usecase.CreateAccountData
import com.vanguard.classifiadmin.onboarding.usecase.LoginData
import com.vanguard.classifiadmin.onboarding.usecase.OnCreateAccountState
import com.vanguard.classifiadmin.onboarding.usecase.OnLoginState


@Composable
fun OnboardingScreen(
    windowSizeClass: WindowSizeClass,
    uiState: OnboardingUiState = rememberOnboardingUiState(),
    onboardingViewModel: OnboardingViewModel = hiltViewModel<OnboardingViewModel>(),
    createAccountViewModel: CreateAccountViewModel = hiltViewModel<CreateAccountViewModel>(),
    loginViewModel: LoginViewModel = hiltViewModel<LoginViewModel>()
) {
    val TAG = "Onboarding"
    val configuration = LocalConfiguration.current
    val navController = (uiState as OnboardingUiState.Success).data.navController
    val state by onboardingViewModel.state.collectAsStateWithLifecycle()
    val createAccountUiState by createAccountViewModel.createAccountUiState.collectAsStateWithLifecycle()
    val loginUiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()

    AlertDialog(
        onDismissRequest = {

        },
        confirmButton = {
            Box(Modifier.padding(horizontal = 8.dp)) {
                ClassifiTextButton(
                    onClick = {
                        when (state.currentDestination) {
                            OnboardingDestination.WELCOME -> {
                                navController.navigateToCreateAccount(onboardingViewModel)
                            }

                            OnboardingDestination.CREATE_ACCOUNT -> {
                                when (createAccountUiState) {
                                    is CreateAccountUiState.Loading -> Unit
                                    is CreateAccountUiState.Success -> {
                                        Log.e(TAG, "OnboardingScreen: create account")
                                        createAccountViewModel.createSuperUser(
                                            createAccountData = CreateAccountData(
                                                email = (createAccountUiState as CreateAccountUiState.Success).data.email,
                                                password = (createAccountUiState as CreateAccountUiState.Success).data.password,
                                                confirmPassword = (createAccountUiState as CreateAccountUiState.Success).data.confirm,
                                            )
                                        ) {
                                            when (it) {
                                                OnCreateAccountState.Success -> {
                                                    Log.e(TAG, "OnboardingScreen: success ")
                                                    navController.navigateToOnboardingSuccessfulScreen(
                                                        onboardingViewModel
                                                    )
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
                                            }
                                        }
                                    }
                                }
                            }

                            OnboardingDestination.LOGIN -> {
                                loginViewModel.loginUser(
                                    loginData = LoginData(
                                        email = (loginUiState as LoginUiState.Success).data.email,
                                        password = (loginUiState as LoginUiState.Success).data.password,
                                    )
                                ) {
                                    Log.e(TAG, "OnboardingScreen: login has been called")
                                    when(it) {
                                        is OnLoginState.Success -> {
                                            Log.e(TAG, "OnboardingScreen: success")
                                            onboardingViewModel.finishOnboarding()
                                        }
                                        is OnLoginState.Failed -> {
                                            Log.e(TAG, "OnboardingScreen: failed")
                                        }

                                        is OnLoginState.NetworkProblem -> {
                                            Log.e(TAG, "OnboardingScreen: network issues", )
                                        }
                                        is OnLoginState.WeakPassword -> {
                                            Log.e(TAG, "OnboardingScreen: weak password")
                                        }
                                        is OnLoginState.UserAlreadyExists -> {
                                            Log.e(TAG, "OnboardingScreen: user already exists")
                                        }
                                        is OnLoginState.InvalidUser -> {
                                            Log.e(TAG, "OnboardingScreen: invalid user")
                                        }
                                        is OnLoginState.InvalidCredentials -> {
                                            Log.e(TAG, "OnboardingScreen: invalid credentials")
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

                            OnboardingDestination.SUCCESS -> {
                                onboardingViewModel.finishOnboarding()
                            }

                            else -> Unit
                        }
                    },
                    text = {
                        val text = when (state.currentDestination) {
                            OnboardingDestination.SUCCESS -> stringResource(id = R.string.finish)
                            else -> stringResource(id = R.string.next)
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
                windowSizeClass = windowSizeClass,
                uiState = uiState,
                createAccountViewModel = createAccountViewModel,
                onboardingViewModel = onboardingViewModel,
                loginViewModel = loginViewModel,
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