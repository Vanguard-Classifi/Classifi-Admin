package com.khalidtouch.chatme.admin.parents

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.khalidtouch.chatme.admin.teachers.KEY_TEACHER_FOR_DETAIL_ID
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.domain.repository.UserRepository
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

const val KEY_PARENT_FOR_DETAIL_ID = "key_parent_for_detail_id"

@HiltViewModel
class ParentScreenViewModel @Inject constructor(
    userRepository: UserRepository,
    userDataRepository: UserDataRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _shouldShowAddParentDialog = MutableStateFlow<Boolean>(false)

    val uiState: StateFlow<ParentScreenUiState> = combine(
        _shouldShowAddParentDialog,
        userDataRepository.userData,
    ) { shouldShowAddParentDialog, data ->
        ParentScreenUiState.Success(
            data = ParentScreenState(
                listOfParents = userRepository.observeParentsFromMySchool(
                    pageSize = 20,
                    schoolId = data.schoolId,
                ),
                shouldShowAddParentDialog = shouldShowAddParentDialog,
                hasParents = true
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ParentScreenUiState.Loading,
    )

    val parentForDetailId: StateFlow<Long> = savedStateHandle.getStateFlow(
        key = KEY_PARENT_FOR_DETAIL_ID,
        initialValue = -1L,
    )

    fun updateAddParentDialogState(state: Boolean) {
        _shouldShowAddParentDialog.value = state
    }

    fun navigateToDetail(newId: Long) {
        savedStateHandle[KEY_PARENT_FOR_DETAIL_ID] = newId
    }
}

sealed interface ParentScreenUiState {
    object Loading: ParentScreenUiState
    data class Success(val data: ParentScreenState): ParentScreenUiState
}

data class ParentScreenState(
    val listOfParents: Flow<PagingData<ClassifiUser>>,
    val shouldShowAddParentDialog: Boolean,
    val hasParents: Boolean,
)