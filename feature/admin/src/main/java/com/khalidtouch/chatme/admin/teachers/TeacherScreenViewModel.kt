package com.khalidtouch.chatme.admin.teachers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.khalidtouch.chatme.domain.repository.UserRepository
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TeacherScreenViewModel @Inject constructor(
    userRepository: UserRepository,
): ViewModel() {
    private val _shouldShowAddTeacherDialog = MutableStateFlow<Boolean>(false)
    private val _finishLoadingState = MutableStateFlow<Boolean>(false)

    val uiState : StateFlow<TeacherScreenUiState> = combine(
        _shouldShowAddTeacherDialog,
        _finishLoadingState
    ) { showDialog, finishLoading ->
        TeacherScreenUiState.Success(
            data = TeacherScreenState(
                shouldShowAddTeacherDialog = showDialog,
                listOfTeachers = userRepository.observeTeachersFromMySchool(20),
                hasFinishedLoading  = finishLoading
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

    fun finishLoading() {
        _finishLoadingState.value = true
    }
}

sealed interface TeacherScreenUiState {
    object Loading: TeacherScreenUiState
    data class Success(val data: TeacherScreenState): TeacherScreenUiState
}


data class TeacherScreenState(
    val shouldShowAddTeacherDialog: Boolean,
    val listOfTeachers: Flow<PagingData<ClassifiUser>>,
    val hasFinishedLoading: Boolean,
)