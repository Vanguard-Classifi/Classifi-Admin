package com.vanguard.classifiadmin.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {
    val TAG= "OnboardingVM"
    private val _currentDestination = MutableStateFlow<OnboardingDestination>(OnboardingDestination.WELCOME)
    val state: StateFlow<OnboardingState> = _currentDestination.map {
        OnboardingState(
            currentDestination = it
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = OnboardingState.Default,
    )

    fun updateCurrentDestination(destination: OnboardingDestination) = viewModelScope.launch {
        _currentDestination.value = destination
    }
}