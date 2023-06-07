package com.khalidtouch.chatme.admin.teachers.addteacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalidtouch.chatme.domain.repository.SchoolRepository
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.network.CreateAccountForTeachers
import com.khalidtouch.classifiadmin.model.UserAccount
import com.khalidtouch.classifiadmin.model.UserRole
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import com.khalidtouch.classifiadmin.model.utils.CreateAccountData
import com.khalidtouch.classifiadmin.model.utils.OnCreateBatchAccountResult
import com.khalidtouch.classifiadmin.model.utils.StageTeacher
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

@HiltViewModel
class AddTeacherViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val schoolRepository: SchoolRepository,
    private val createAccountForTeachers: CreateAccountForTeachers
) : ViewModel() {

    private val _email = MutableStateFlow<String>("")
    private val _password = MutableStateFlow<String>("")
    private val _confirmPassword = MutableStateFlow<String>("")
    private val _stagedTeachers = MutableStateFlow<ArrayList<StageTeacher>>(arrayListOf())
    private val _currentPage = MutableStateFlow<AddTeacherPage>(AddTeacherPage.INPUT)

    val observeMySchool: StateFlow<ClassifiSchool?> = userDataRepository.userData.map {
        val schoolId = it.schoolId
        schoolRepository.fetchSchoolById(schoolId)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
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
        if (_stagedTeachers.value.find { it.teacher.account?.email == email } != null) return
        _stagedTeachers.value.add(
            StageTeacher(
                teacher = ClassifiUser(
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

    fun onRemoveTeacher(teacher: StageTeacher) {
        _stagedTeachers.value.remove(teacher)
    }


    fun onClearStage() {
        _stagedTeachers.value.clear()
    }

    fun onNavigate(to: AddTeacherPage) {
        _currentPage.value = to
    }

    fun createAccountForTeachers(teachers: List<StageTeacher>, result: OnCreateBatchAccountResult) {
        createAccountForTeachers.createAccountForTeachers(
            teachers = teachers,
            result = result
        )
    }
}


data class AddTeacherState(
    val email: String,
    val password: String,
    val confirmPassword: String,
    val stagedTeachers: List<StageTeacher>,
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
