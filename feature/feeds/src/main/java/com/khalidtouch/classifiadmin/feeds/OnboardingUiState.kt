package com.khalidtouch.classifiadmin.feeds

sealed interface OnboardingUiState {
    object Loading : OnboardingUiState

    object LoadFailed : OnboardingUiState

    object NotShown : OnboardingUiState

    object Shown : OnboardingUiState
}