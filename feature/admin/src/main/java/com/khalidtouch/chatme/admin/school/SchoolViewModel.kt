package com.khalidtouch.chatme.admin.school

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.khalidtouch.chatme.domain.repository.SchoolRepository
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class SchoolViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val schoolRepository: SchoolRepository,
) : ViewModel() {
    private val _addSchoolDialogState = MutableStateFlow<Boolean>(false)
    private val _hasFinishedLoading = MutableStateFlow<Boolean>(false)

    val uiState: StateFlow<SchoolScreenUiState> = combine(
        _addSchoolDialogState,
        _hasFinishedLoading,
    ) { dialogState, finishedLoading ->
        SchoolScreenUiState.Success(
            data = SchoolScreenData(
                shouldShowAddSchoolDialog = dialogState,
                hasFinishedLoading = finishedLoading,
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SchoolScreenUiState.Loading
    )

    val observeMySchool: StateFlow<ClassifiSchool?> = userDataRepository.userData.map {
        val schoolId = it.schoolId
        schoolRepository.fetchSchoolById(schoolId)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    fun onShowAddSchoolDialog() {
        _addSchoolDialogState.value = true
    }

    fun onHideAddSchoolDialog() {
        _addSchoolDialogState.value = false
    }

    fun onFinishLoadingState() {
        _hasFinishedLoading.value = true
    }
}


sealed interface SchoolScreenUiState {
    object Loading : SchoolScreenUiState
    data class Success(val data: SchoolScreenData) : SchoolScreenUiState
}

data class SchoolScreenData(
    val shouldShowAddSchoolDialog: Boolean,
    val hasFinishedLoading: Boolean,
)