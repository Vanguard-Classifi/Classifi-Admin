package com.khalidtouch.chatme.admin.teachers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TeacherScreenViewModel @Inject constructor(): ViewModel() {
    private val _shouldShowAddTeacherDialog = MutableStateFlow<Boolean>(false)

    val uiState : StateFlow<TeacherScreenUiState> = _shouldShowAddTeacherDialog.map {
        TeacherScreenUiState.Success(
            data = TeacherScreenState(
                shouldShowAddTeacherDialog = it
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TeacherScreenUiState.Loading,
    )

    fun onSetAddTeacherDialogState(state: Boolean) {
        _shouldShowAddTeacherDialog.value  = state
    }
}

sealed interface TeacherScreenUiState {
    object Loading: TeacherScreenUiState
    data class Success(val data: TeacherScreenState): TeacherScreenUiState
}


data class TeacherScreenState(
    val shouldShowAddTeacherDialog: Boolean,
)