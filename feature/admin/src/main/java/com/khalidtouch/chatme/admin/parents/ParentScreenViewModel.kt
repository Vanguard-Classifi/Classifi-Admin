package com.khalidtouch.chatme.admin.parents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
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

@HiltViewModel
class ParentScreenViewModel @Inject constructor(
    userRepository: UserRepository,
    userDataRepository: UserDataRepository,
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
                shouldShowAddParentDialog = shouldShowAddParentDialog
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ParentScreenUiState.Loading,
    )

    fun updateAddParentDialogState(state: Boolean) {
        _shouldShowAddParentDialog.value = state
    }
}

sealed interface ParentScreenUiState {
    object Loading: ParentScreenUiState
    data class Success(val data: ParentScreenState): ParentScreenUiState
}

data class ParentScreenState(
    val listOfParents: Flow<PagingData<ClassifiUser>>,
    val shouldShowAddParentDialog: Boolean,
)