package com.vanguard.classifiadmin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.khalidtouch.chatme.domain.repository.SchoolRepository
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.domain.repository.UserRepository
import com.khalidtouch.classifiadmin.model.UserData
import com.vanguard.classifiadmin.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val userRepository: UserRepository,
    private val schoolRepository: SchoolRepository,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> =
        userDataRepository.userData.map { data ->
            MainActivityUiState.Success(
                data = MainActivityData(
                    userData = data,
                    isCurrentlySignedIn = !data.shouldReAuthenticate
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = MainActivityUiState.Loading,
        )

    fun forceShowOnboarding() = viewModelScope.launch {
        userDataRepository.setShouldHideOnboarding(false)
    }

    val forceObserveMyId: StateFlow<Long> = userDataRepository.userData.map {
        it.userId
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = -1,
    )

    fun forceClearUsers() = viewModelScope.launch {
        userRepository.deleteAllUsers()
    }

    fun forceLogout() = viewModelScope.launch {
        val auth = Firebase.auth
        auth.signOut()
    }

    fun forceClearSchools() = viewModelScope.launch {
        schoolRepository.deleteAllSchools()
    }

    fun updateReAuthenticationState(state: Boolean) = viewModelScope.launch {
        userDataRepository.updateReAuthenticationState(state)
    }
}


sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(val data: MainActivityData) : MainActivityUiState
}

data class MainActivityData(
    val userData: UserData,
    val isCurrentlySignedIn: Boolean,
)