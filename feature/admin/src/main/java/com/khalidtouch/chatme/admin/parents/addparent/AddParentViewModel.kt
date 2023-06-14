package com.khalidtouch.chatme.admin.parents.addparent

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

const val KEY_NUMBER_OF_PARENTS_REGISTERED = "key_number_of_parents_registered"
const val KEY_NUMBER_OF_PARENTS_FAILED = "key_number_of_parents_failed"


@HiltViewModel
class AddParentViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    private val schoolRepository: SchoolRepository,
    private val savedStateHandle: SavedStateHandle,
    private val createAccountForParents: CreateAccountForUsers,
) : ViewModel() {

    private val _email = MutableStateFlow<String>("")
    private val _password = MutableStateFlow<String>("")
    private val _confirmPassword = MutableStateFlow<String>("")
    private val _stagedParents = MutableStateFlow<ArrayList<StagedUser>>(arrayListOf())
    private val _currentPage = MutableStateFlow<AddParentPage>(AddParentPage.INPUT)
    private val _registeringParentProgressBar = MutableStateFlow<Boolean>(false)
    private val _registeringParentSnackBar = MutableStateFlow<Boolean>(false)
    private val _registeringParentMessage = MutableStateFlow<String>("")
    private val _currentStagedUserId = MutableStateFlow<Long>(-1L)


    val unStagingEnabled: StateFlow<Boolean> = combine(
        _stagedParents,
        _currentStagedUserId
    ) { stagedParents, currentUserId ->
        stagedParents.isNotEmpty() && currentUserId != -1L
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

    val numberOfParentsRegistered: StateFlow<Int> = savedStateHandle.getStateFlow(
        key = KEY_NUMBER_OF_PARENTS_REGISTERED, initialValue = 0
    )

    val numberOfParentsFailed: StateFlow<Int> = savedStateHandle.getStateFlow(
        key = KEY_NUMBER_OF_PARENTS_FAILED, initialValue = 0
    )

    val registeringParentState: StateFlow<RegisteringParentState> = combine(
        _registeringParentMessage,
        _registeringParentSnackBar,
        _registeringParentProgressBar,
        _currentStagedUserId,
    ) { message, snackbar, progressBar, userId ->
        RegisteringParentState(
            progressBarState = progressBar,
            snackbarState = snackbar,
            message = message,
            currentStagedUserId = userId,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RegisteringParentState.DEFAULT,
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

    fun onStageParent(
        addParentState: AddParentState,
        mySchool: ClassifiSchool?,
    ) {
        if (_stagedParents.value.find { it.user.account?.email == addParentState.email } != null) return
        _stagedParents.value.add(
            StagedUser(
                user = ClassifiUser(
                    userId = System.currentTimeMillis() + addParentState.email.hashCode(),
                    account = UserAccount(
                        email = addParentState.email,
                        userRole = UserRole.Parent,
                    ),
                    dateCreated = LocalDateTime.now(),
                    joinedSchools = if (mySchool == null) {
                        listOf<ClassifiSchool>()
                    } else {
                        listOf(mySchool)
                    }
                ),
                password = addParentState.password,
                confirmPassword = addParentState.confirmPassword
            )
        )
        _email.value = ""
        _password.value = ""
        _confirmPassword.value = ""
    }

    fun unStageParent(parentId: Long) {
        val parent = _stagedParents.value.find { it.user.userId == parentId } ?: return
        _stagedParents.value.remove(parent)
        _currentStagedUserId.value = -1L
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
        createAccountForParents.createAccountForUsers(
            mySchool = mySchool,
            users = parents,
            result = result
        )
    }

    fun cacheNumberOfParentsRegistered(number: Int) {
        savedStateHandle[KEY_NUMBER_OF_PARENTS_REGISTERED] = number
    }

    fun cacheNumberOfParentsFailed(number: Int) {
        savedStateHandle[KEY_NUMBER_OF_PARENTS_FAILED] = number
    }

    fun updateRegisteringParentsProgressBarState(state: Boolean) {
        _registeringParentProgressBar.value = state
    }

    fun updateRegisteringParentsSnackbarState(state: Boolean) {
        _registeringParentSnackBar.value = state
    }

    fun updateRegisteringParentMessage(message: String) {
        _registeringParentMessage.value = message
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

data class RegisteringParentState(
    val progressBarState: Boolean,
    val snackbarState: Boolean,
    val message: String,
    val currentStagedUserId: Long,
) {
    companion object {
        val DEFAULT = RegisteringParentState(
            progressBarState = false,
            snackbarState = false,
            message = "",
            currentStagedUserId = -1L,
        )
    }
}
