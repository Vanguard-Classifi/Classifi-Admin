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


@Composable
fun OnboardingScreen(
    windowSizeClass: WindowSizeClass,
    uiState: OnboardingUiState = rememberOnboardingUiState(),
    onboardingViewModel: OnboardingViewModel = hiltViewModel<OnboardingViewModel>()
) {
    val TAG = "Onboarding"
    val configuration = LocalConfiguration.current
    val navController = (uiState as OnboardingUiState.Success).data.navController
    val state by onboardingViewModel.state.collectAsStateWithLifecycle()

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

                            else -> Unit
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(id = R.string.next),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                )
            }
        },
        dismissButton = {
            Log.e(TAG, "OnboardingScreen: current destination ${state.currentDestination.name}")
            when (state.currentDestination) {
                OnboardingDestination.WELCOME -> Unit
                else -> {
                    Box(Modifier.padding(horizontal = 8.dp)) {
                        ClassifiTextButton(
                            onClick = { /*TODO*/ },
                            text = {
                                Text(
                                    text = stringResource(id = R.string.back),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        )
                    }
                }
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                OnboardingNavHost(
                    windowSizeClass = windowSizeClass,
                    uiState = uiState,
                )
            }
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