package com.khalidtouch.chatme.admin.school

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class SchoolViewModel @Inject constructor() : ViewModel() {
    private val _addSchoolDialogState = MutableStateFlow<Boolean>(false)

    val uiState: StateFlow<SchoolScreenUiState> = _addSchoolDialogState.map {
        SchoolScreenUiState.Success(
            data = SchoolScreenData(
                shouldShowAddSchoolDialog = it
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SchoolScreenUiState.Loading
    )

    fun onShowAddSchoolDialog() {
        _addSchoolDialogState.value = true
    }

    fun onHideAddSchoolDialog() {
        _addSchoolDialogState.value = false
    }
}


sealed interface SchoolScreenUiState {
    object Loading : SchoolScreenUiState
    data class Success(val data: SchoolScreenData) : SchoolScreenUiState
}

data class SchoolScreenData(
    val shouldShowAddSchoolDialog: Boolean,
)