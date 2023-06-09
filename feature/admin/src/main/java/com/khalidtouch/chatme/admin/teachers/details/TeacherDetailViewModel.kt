package com.khalidtouch.chatme.admin.teachers.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
) : ViewModel() {

    private val _teacherForDetail = MutableStateFlow<ClassifiUser?>(null)
    private val _shouldShowExpandedImage = MutableStateFlow<Boolean>(false)

    val uiState: StateFlow<TeacherDetailUiState> = combine(
        _teacherForDetail,
        _shouldShowExpandedImage
    ) { teacher, showExpandedImage ->
        TeacherDetailUiState.Success(
            data = TeacherDetailState(
                teacher = teacher,
                shouldShowExpandedImage = showExpandedImage
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
}

sealed interface TeacherDetailUiState {
    object Loading: TeacherDetailUiState
    data class Success(val data: TeacherDetailState): TeacherDetailUiState
}

data class TeacherDetailState(
    val teacher: ClassifiUser?,
    val shouldShowExpandedImage: Boolean,
)