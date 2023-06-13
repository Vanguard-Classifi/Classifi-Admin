package com.khalidtouch.chatme.admin.parents.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.domain.repository.UserRepository
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ParentDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    userDataRepository: UserDataRepository,
) : ViewModel() {
    private val _parentForDetail = MutableStateFlow<ClassifiUser?>(null)
    private val _shouldShowExpandedImage = MutableStateFlow<Boolean>(false)
    private val _shouldShowParentMenu = MutableStateFlow<Boolean>(false)
    private val _shouldShowDeleteDialog = MutableStateFlow<Boolean>(false)
    private val _email = MutableStateFlow<String>("")

    val mySchoolId: StateFlow<Long> = userDataRepository.userData.map {
        it.schoolId
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = -1L,
    )

    val uiState: StateFlow<ParentDetailUiState> = combine(
        _parentForDetail,
        _shouldShowExpandedImage,
        _shouldShowParentMenu,
        _shouldShowDeleteDialog,
        _email,
    ) { parent, showExpandedImage, showMenu, showDeleteDialog, email ->
        ParentDetailUiState.Success(
            data = ParentDetailState(
                parent = parent,
                shouldShowExpandedImage = showExpandedImage,
                shouldShowParentMenu = showMenu,
                shouldShowDeleteDialog = showDeleteDialog,
                canBeDeleted = email == parent?.account?.email,
                email = email,
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ParentDetailUiState.Loading,
    )

    fun loadParentInfo(id: Long) {
        viewModelScope.launch {
            val parent = userRepository.fetchUserById(id)
            _parentForDetail.value = parent
        }
    }

    fun updateExpandedImageState(state: Boolean) {
        _shouldShowExpandedImage.value = state
    }

    fun updateParentMenuState(state: Boolean) {
        _shouldShowParentMenu.value = state
    }

    fun updateDeleteDialogState(state: Boolean) {
        _shouldShowDeleteDialog.value = state
        _email.value = ""
    }

    fun onEmailChanged(email: String) {
        _email.value = email
    }

    fun unregisterParentFromSchool(parentId: Long, schoolId: Long) {
        try {
            viewModelScope.launch {
                userRepository.unregisterUserFromSchool(
                    parentId,
                    schoolId,
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}


sealed interface ParentDetailUiState {
    object Loading : ParentDetailUiState
    data class Success(val data: ParentDetailState) : ParentDetailUiState
}

data class ParentDetailState(
    val parent: ClassifiUser?,
    val shouldShowExpandedImage: Boolean,
    val shouldShowParentMenu: Boolean,
    val shouldShowDeleteDialog: Boolean,
    val email: String,
    val canBeDeleted: Boolean,
)