package com.khalidtouch.chatme.admin.parents.addparent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalidtouch.chatme.domain.repository.SchoolRepository
import com.khalidtouch.chatme.domain.repository.UserDataRepository
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


@HiltViewModel
class AddParentViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val schoolRepository: SchoolRepository,
): ViewModel() {

    private val _email = MutableStateFlow<String>("")
    private val _password = MutableStateFlow<String>("")
    private val _confirmPassword = MutableStateFlow<String>("")
    private val _stagedParents = MutableStateFlow<ArrayList<StagedUser>>(arrayListOf())
    private val _currentPage = MutableStateFlow<AddParentPage>(AddParentPage.INPUT)

    val observeMySchool: StateFlow<ClassifiSchool?> = userDataRepository.userData.map {
        val schoolId = it.schoolId
        schoolRepository.fetchSchoolById(schoolId)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )


    val state: StateFlow<AddParentState> = combine(
        _email,
        _password,
        _confirmPassword,
        _stagedParents,
        _currentPage,
    ) { email, password, confirm, staged, currentPage ->
        val canAddMoreParents = email.isNotBlank() && password.isNotBlank() && confirm.isNotBlank()
                && email.isEmailValid() && password.isPasswordValid() && password == confirm

        AddParentState(
            email = email,
            password = password,
            confirmPassword = confirm,
            stagedParents = staged,
            canAddMoreParents = canAddMoreParents,
            currentPage = currentPage,
            canSubmitParentsInfo = canAddMoreParents || staged.isNotEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddParentState.Default
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

    fun onStageParent(
        email: String,
        password: String,
        confirmPassword: String,
        mySchool: ClassifiSchool?,
    ) {
        if (_stagedParents.value.find { it.user.account?.email == email } != null) return
        _stagedParents.value.add(
            StagedUser(
                user = ClassifiUser(
                    userId = System.currentTimeMillis() + email.hashCode(),
                    account = UserAccount(
                        email = email,
                        userRole = UserRole.Parent,
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

    fun onRemoveParent(parent: StagedUser) {
        _stagedParents.value.remove(parent)
    }


    fun onClearStage() {
        _stagedParents.value.clear()
    }

    fun onNavigate(to: AddParentPage) {
        _currentPage.value = to
    }

    fun createAccountForParents(
        mySchool: ClassifiSchool?,
        parents: List<StagedUser>,
        result: OnCreateBatchAccountResult
    ) {
      /*todo() */
    }

}

data class AddParentState(
    val email: String,
    val password: String,
    val confirmPassword: String,
    val stagedParents: List<StagedUser>,
    val canAddMoreParents: Boolean,
    val currentPage: AddParentPage,
    val canSubmitParentsInfo: Boolean,
) {
    companion object {
        val Default = AddParentState(
            email = "",
            password = "",
            confirmPassword = "",
            stagedParents = listOf(),
            canAddMoreParents = false,
            currentPage = AddParentPage.INPUT,
            canSubmitParentsInfo = false,
        )
    }
}

enum class AddParentPage { INPUT, SUCCESS, }