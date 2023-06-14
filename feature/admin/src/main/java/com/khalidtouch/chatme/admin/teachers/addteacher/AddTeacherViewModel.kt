package com.khalidtouch.chatme.admin.teachers.addteacher

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalidtouch.chatme.domain.repository.SchoolRepository
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.network.CreateAccountForUsers
import com.khalidtouch.classifiadmin.model.UserAccount
import com.khalidtouch.classifiadmin.model.UserRole
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import com.khalidtouch.classifiadmin.model.utils.OnCreateBatchAccountResult
import com.khalidtouch.classifiadmin.model.utils.StagedUser
import com.khalidtouch.core.common.extensions.isEmailValid
import com.khalidtouch.core.common.extensions.isPasswordValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import javax.inject.Inject

const val KEY_NUMBER_OF_TEACHERS_REGISTERED = "key_number_of_teachers_registered"
const val KEY_NUMBER_OF_TEACHERS_FAILED = "key_number_of_teachers_failed"

@HiltViewModel
class AddTeacherViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    private val schoolRepository: SchoolRepository,
    private val createAccountForTeachers: CreateAccountForUsers,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _email = MutableStateFlow<String>("")
    private val _password = MutableStateFlow<String>("")
    private val _confirmPassword = MutableStateFlow<String>("")
    private val _stagedTeachers = MutableStateFlow<ArrayList<StagedUser>>(arrayListOf())
    private val _currentPage = MutableStateFlow<AddTeacherPage>(AddTeacherPage.INPUT)
    private val _registeringTeacherProgressBar = MutableStateFlow<Boolean>(false)
    private val _registeringTeacherSnackBar = MutableStateFlow<Boolean>(false)
    private val _registeringTeacherMessage = MutableStateFlow<String>("")
    private val _currentStagedUserId = MutableStateFlow<Long>(-1L)


    val unStagingEnabled: StateFlow<Boolean> = combine(
        _stagedTeachers,
        _currentStagedUserId
    ) { stagedTeachers, currentUserId ->
        stagedTeachers.isNotEmpty() && currentUserId != -1L
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false,
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

    val numberOfTeachersRegistered: StateFlow<Int> = savedStateHandle.getStateFlow(
        key = KEY_NUMBER_OF_TEACHERS_REGISTERED, initialValue = 0
    )

    val numberOfTeachersFailed: StateFlow<Int> = savedStateHandle.getStateFlow(
        key = KEY_NUMBER_OF_TEACHERS_FAILED, initialValue = 0
    )

    val registeringTeacherState: StateFlow<RegisteringTeacherState> = combine(
        _registeringTeacherMessage,
        _registeringTeacherSnackBar,
        _registeringTeacherProgressBar,
        _currentStagedUserId,
    ) { message, snackbar, progressBar, userId ->
        RegisteringTeacherState(
            progressBarState = progressBar,
            snackbarState = snackbar,
            message = message,
            currentStagedUserId = userId,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RegisteringTeacherState.DEFAULT,
    )

    val state: StateFlow<AddTeacherState> = combine(
        _email,
        _password,
        _confirmPassword,
        _stagedTeachers,
        _currentPage,
    ) { email, password, confirm, staged, currentPage ->
        val canAddMoreTeachers = email.isNotBlank() && password.isNotBlank() && confirm.isNotBlank()
                && email.isEmailValid() && password.isPasswordValid() && password == confirm

        AddTeacherState(
            email = email,
            password = password,
            confirmPassword = confirm,
            stagedTeachers = staged,
            canAddMoreTeachers = canAddMoreTeachers,
            currentPage = currentPage,
            canSubmitTeachersInfo = canAddMoreTeachers || staged.isNotEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddTeacherState.Default
    )

    fun onEmailChanged(email: String) {
        _email.value = email
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    fun onConfirmPasswordChanged(confirm: String) {
        _confirmPassword.value = confirm
    }

    fun onStageTeacher(
        email: String,
        password: String,
        confirmPassword: String,
        mySchool: ClassifiSchool?,
    ) {
        if (_stagedTeachers.value.find { it.user.account?.email == email } != null) return
        _stagedTeachers.value.add(
            StagedUser(
                user = ClassifiUser(
                    userId = System.currentTimeMillis() + email.hashCode(),
                    account = UserAccount(
                        email = email,
                        userRole = UserRole.Teacher,
                    ),
                    dateCreated = LocalDateTime.now(),
                    joinedSchools = if (mySchool == null) {
                        listOf<ClassifiSchool>()
                    } else {
                        listOf(mySchool)
                    }
                ),
                password = password,
                confirmPassword = confirmPassword
            )
        )
        _email.value = ""
        _password.value = ""
        _confirmPassword.value = ""
    }

    fun onRemoveTeacher(teacherId: Long) {
        val teacher = _stagedTeachers.value.find { it.user.userId == teacherId } ?: return
        _stagedTeachers.value.remove(teacher)
        _currentStagedUserId.value = -1L
    }


    fun onClearStage() {
        _stagedTeachers.value.clear()
    }

    fun onNavigate(to: AddTeacherPage) {
        _currentPage.value = to
    }

    fun createAccountForTeachers(
        mySchool: ClassifiSchool?,
        teachers: List<StagedUser>,
        result: OnCreateBatchAccountResult
    ) {
        createAccountForTeachers.createAccountForUsers(
            users = teachers,
            result = result,
            mySchool = mySchool
        )
    }

    fun cacheNumberOfTeachersRegistered(number: Int) {
        savedStateHandle[KEY_NUMBER_OF_TEACHERS_REGISTERED] = number
    }

    fun cacheNumberOfTeachersFailed(number: Int) {
        savedStateHandle[KEY_NUMBER_OF_TEACHERS_FAILED] = number
    }

    fun updateRegisteringTeachersProgressBarState(state: Boolean) {
        _registeringTeacherProgressBar.value = state
    }

    fun updateRegisteringTeachersSnackbarState(state: Boolean) {
        _registeringTeacherSnackBar.value = state
    }

    fun updateRegisteringTeacherMessage(message: String) {
        _registeringTeacherMessage.value = message
    }

    fun updateFieldsWithCurrentUser(user: StagedUser) {
        _email.value = user.user.account?.email.orEmpty()
        _password.value = user.password
        _confirmPassword.value = user.confirmPassword
        updateCurrentStagedUserId(user.user.userId ?: -1L)
    }

    fun updateCurrentStagedUserId(id: Long) {
        if (_currentStagedUserId.value == id) {
            _currentStagedUserId.value = -1L
            return
        }
        _currentStagedUserId.value = id
    }
}


data class AddTeacherState(
    val email: String,
    val password: String,
    val confirmPassword: String,
    val stagedTeachers: List<StagedUser>,
    val canAddMoreTeachers: Boolean,
    val currentPage: AddTeacherPage,
    val canSubmitTeachersInfo: Boolean,
) {
    companion object {
        val Default = AddTeacherState(
            email = "",
            password = "",
            confirmPassword = "",
            stagedTeachers = listOf(),
            canAddMoreTeachers = false,
            currentPage = AddTeacherPage.INPUT,
            canSubmitTeachersInfo = false,
        )
    }
}


enum class AddTeacherPage {
    INPUT, SUCCESS
}


data class RegisteringTeacherState(
    val progressBarState: Boolean,
    val snackbarState: Boolean,
    val message: String,
    val currentStagedUserId: Long,
) {
    companion object {
        val DEFAULT = RegisteringTeacherState(
            progressBarState = false,
            snackbarState = false,
            message = "",
            currentStagedUserId = -1L,
        )
    }
}

