package com.khalidtouch.chatme.admin.teachers

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.launch
import javax.inject.Inject

const val KEY_TEACHER_FOR_DETAIL_ID = "key_teacher_for_detail_id"

@HiltViewModel
class TeacherScreenViewModel @Inject constructor(
    userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _shouldShowAddTeacherDialog = MutableStateFlow<Boolean>(false)
    private val _finishLoadingState = MutableStateFlow<Boolean>(false)
    private val _selectedTeachers = MutableStateFlow<ArrayList<Long>>(arrayListOf())
    private val _teacherSelectionListener = MutableStateFlow<Int>(0)
    val teacherSelectionListener: StateFlow<Int> = _teacherSelectionListener
    val teacherForDetailId: StateFlow<Long> = savedStateHandle.getStateFlow(
        key = KEY_TEACHER_FOR_DETAIL_ID, initialValue = -1L,
    )

    val uiState: StateFlow<TeacherScreenUiState> = combine(
        _shouldShowAddTeacherDialog,
        _finishLoadingState,
        _selectedTeachers,
    ) { showDialog, finishLoading, selectedTeachers ->
        TeacherScreenUiState.Success(
            data = TeacherScreenState(
                shouldShowAddTeacherDialog = showDialog,
                listOfTeachers = userRepository.observeTeachersFromMySchool(20),
                hasFinishedLoading = finishLoading,
                hasTeachers = true,
                selectedTeachers = selectedTeachers,
                shouldShowExtraFeaturesOnTopBar = selectedTeachers.isNotEmpty()
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TeacherScreenUiState.Loading,
    )

    fun onSetAddTeacherDialogState(state: Boolean) {
        _shouldShowAddTeacherDialog.value = state
    }

    fun finishLoading() {
        _finishLoadingState.value = true
    }

    fun listenForTeacherSelection() {
        _teacherSelectionListener.value.plus(1)
    }

    fun onSelectTeacherOrDeselect(teacherId: Long) {
        viewModelScope.launch {
            if (_selectedTeachers.value.contains(teacherId)) {
                _selectedTeachers.value.remove(teacherId)
            }
            _selectedTeachers.value.add(teacherId)
        }
    }

    fun navigateToDetail(newId: Long) {
        savedStateHandle[KEY_TEACHER_FOR_DETAIL_ID] = newId
    }
}

sealed interface TeacherScreenUiState {
    object Loading : TeacherScreenUiState
    data class Success(val data: TeacherScreenState) : TeacherScreenUiState
}

data class TeacherScreenState(
    val shouldShowAddTeacherDialog: Boolean,
    val listOfTeachers: Flow<PagingData<ClassifiUser>>,
    val hasTeachers: Boolean,
    val hasFinishedLoading: Boolean,
    val selectedTeachers: List<Long>,
    val shouldShowExtraFeaturesOnTopBar: Boolean,
)