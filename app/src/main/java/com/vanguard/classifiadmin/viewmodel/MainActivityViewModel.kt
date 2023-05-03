package com.vanguard.classifiadmin.viewmodel

import androidx.lifecycle.ViewModel
import com.khalidtouch.classifiadmin.model.UserData
import com.vanguard.classifiadmin.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {

}


sealed interface MainActivityUiState {
    object Loading: MainActivityUiState
    data class Success(val userData: UserData): MainActivityUiState
}