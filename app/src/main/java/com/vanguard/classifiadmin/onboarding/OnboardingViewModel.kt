package com.vanguard.classifiadmin.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.vanguard.classifiadmin.onboarding.usecase.CreateAccountData
import com.vanguard.classifiadmin.onboarding.usecase.OnCreateAccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {
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

    fun finishOnboarding() = viewModelScope.launch {
        userDataRepository.setShouldHideOnboarding(true)
    }

}