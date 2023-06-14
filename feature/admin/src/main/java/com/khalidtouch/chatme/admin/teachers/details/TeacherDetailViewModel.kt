package com.khalidtouch.chatme.admin.teachers.details

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
class TeacherDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    userDataRepository: UserDataRepository,
) : ViewModel() {

    private val _teacherForDetail = MutableStateFlow<ClassifiUser?>(null)
    private val _shouldShowExpandedImage = MutableStateFlow<Boolean>(false)
    private val _shouldShowTeacherMenu = MutableStateFlow<Boolean>(false)
    private val _shouldShowDeleteDialog = MutableStateFlow<Boolean>(false)
    private val _email = MutableStateFlow<String>("")

    val mySchoolId: StateFlow<Long> = userDataRepository.userData.map {
        it.schoolId
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = -1L,
    )

    val uiState: StateFlow<TeacherDetailUiState> = combine(
        _teacherForDetail,
        _shouldShowExpandedImage,
        _shouldShowTeacherMenu,
        _shouldShowDeleteDialog,
        _email,
    ) { teacher, showExpandedImage, showMenu, showDeleteDialog, email ->
        TeacherDetailUiState.Success(
            data = TeacherDetailState(
                teacher = teacher,
                shouldShowExpandedImage = showExpandedImage,
                shouldShowTeacherMenu = showMenu,
                shouldShowDeleteDialog = showDeleteDialog,
                canBeDeleted = email == teacher?.account?.email,
                email = email,
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TeacherDetailUiState.Loading,
    )


    fun loadTeacherInfo(id: Long) {
        viewModelScope.launch {
            val teacher = userRepository.fetchUserById(id)
            _teacherForDetail.value = teacher
        }
    }


    fun updateExpandedImageState(state: Boolean) {
        _shouldShowExpandedImage.value = state
    }

    fun updateTeacherMenuState(state: Boolean) {
        _shouldShowTeacherMenu.value = state
    }

    fun updateDeleteDialogState(state: Boolean) {
        _shouldShowDeleteDialog.value = state
        _email.value = ""
    }

    fun onEmailChanged(email: String) {
        _email.value = email
    }

    fun unregisterParentFromSchool(teacherId: Long, schoolId: Long) {
        try {
            viewModelScope.launch {
                userRepository.unregisterUserFromSchool(
                    teacherId,
                    schoolId,
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

sealed interface TeacherDetailUiState {
    object Loading : TeacherDetailUiState
    data class Success(val data: TeacherDetailState) : TeacherDetailUiState
}

data class TeacherDetailState(
    val teacher: ClassifiUser?,
    val shouldShowExpandedImage: Boolean,
    val shouldShowTeacherMenu: Boolean,
    val shouldShowDeleteDialog: Boolean,
    val email: String,
    val canBeDeleted: Boolean,
)