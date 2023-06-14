package com.vanguard.classifiadmin.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanguard.classifiadmin.onboarding.usecase.LoginData
import com.vanguard.classifiadmin.onboarding.usecase.LoginUseCase
import com.vanguard.classifiadmin.onboarding.usecase.OnLoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    val TAG = "LoginVM"
    private val email = MutableStateFlow<String>("")
    private val password = MutableStateFlow<String>("")
    val loginUiState: StateFlow<LoginUiState> = combine(
        email, password
    ) { email, password ->
        LoginUiState.Success(
            data = LoginCredentials(
                email = email,
                password = password,
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LoginUiState.Loading
    )

    fun onEmailChanged(_email: String) {
        email.value = _email
    }

    fun onPasswordChanged(_password: String) {
        password.value = _password
    }

    fun onLogin(
        loginData: LoginData,
        callback: (OnLoginState) -> Unit,
    )  = viewModelScope.launch {
        loginUseCase(
            loginData = loginData,
            callback
        )
    }

}

sealed interface LoginUiState {
    object Loading : LoginUiState
    data class Success(val data: LoginCredentials) : LoginUiState
}

data class LoginCredentials(
    val email: String,
    val password: String,
)