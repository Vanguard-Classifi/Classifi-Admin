package com.vanguard.classifiadmin.onboarding

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope

sealed interface OnboardingUiState {
    object Loading : OnboardingUiState
    data class Success(val data: OnboardingData) : OnboardingUiState
}

data class OnboardingData(
    val navController: NavHostController,
    val scope: CoroutineScope,
)

data class OnboardingState(
    var currentDestination: OnboardingDestination,
) {
    companion object {
        val Default = OnboardingState(
            currentDestination = OnboardingDestination.WELCOME
        )
    }
}

enum class OnboardingDestination {
    WELCOME, CREATE_ACCOUNT, LOGIN, SUCCESS
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberOnboardingUiState(
    navController: NavHostController = rememberAnimatedNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): OnboardingUiState {
    return remember(navController, coroutineScope) {
        OnboardingUiState.Success(
            data = OnboardingData(
                navController = navController,
                scope = coroutineScope,
            )
        )
    }
}
