package com.vanguard.classifiadmin.onboarding

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanguard.classifiadmin.onboarding.usecase.CreateAccountData
import com.vanguard.classifiadmin.onboarding.usecase.CreateAccountForSuperAdminUseCase
import com.vanguard.classifiadmin.onboarding.usecase.OnCreateAccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val createAccountForSuperAdminUseCase: CreateAccountForSuperAdminUseCase
) : ViewModel() {

    val TAG = "CAVM"
    private val email = MutableStateFlow<String>("")
    private val password = MutableStateFlow<String>("")
    private val confirmPassword = MutableStateFlow<String>("")
    private val hasAuthenticated = MutableStateFlow<Boolean>(false)
    val createAccountUiState: StateFlow<CreateAccountUiState> = combine(
        email,
        password,
        confirmPassword,
        hasAuthenticated
    ) { email, password, confirm, hasAuth ->
        CreateAccountUiState.Success(
            data = Credentials(
                email = email,
                password = password,
                confirm = confirm,
                hasAuthenticated = hasAuth,
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CreateAccountUiState.Loading
    )


    fun onEmailChanged(_email: String) {
        email.value = _email
        Log.e(TAG, "onEmailChanged: email is ${email}")
    }

    fun onPasswordChanged(_password: String) {
        password.value = _password
        Log.e(TAG, "onPasswordChanged: password is $password")
    }

    fun onConfirmPasswordChanged(_confirm: String) {
        confirmPassword.value = _confirm
        Log.e(TAG, "onConfirmPasswordChanged: confirmPassword is $confirmPassword")
    }

    fun onAuthStateChanged(state: Boolean) {
        hasAuthenticated.value = state
    }

    fun createSuperUser(
        createAccountData: CreateAccountData,
        callback: (OnCreateAccountState) -> Unit
    ) = viewModelScope.launch {
        createAccountForSuperAdminUseCase(
            data = createAccountData,
            callback
        )
    }
}


sealed interface CreateAccountUiState {
    object Loading : CreateAccountUiState
    data class Success(val data: Credentials) : CreateAccountUiState
}

data class Credentials(
    val email: String,
    val password: String,
    val confirm: String,
    val hasAuthenticated: Boolean,
)