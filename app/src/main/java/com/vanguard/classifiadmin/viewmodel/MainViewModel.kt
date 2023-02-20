package com.vanguard.classifiadmin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.vanguard.classifiadmin.data.local.models.SchoolModel
import com.vanguard.classifiadmin.data.local.models.UserModel
import com.vanguard.classifiadmin.data.network.models.SchoolNetworkModel
import com.vanguard.classifiadmin.data.network.models.UserNetworkModel
import com.vanguard.classifiadmin.data.preferences.PrefDatastore
import com.vanguard.classifiadmin.data.repository.MainRepository
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.UserLoginState
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentOption
import com.vanguard.classifiadmin.ui.screens.classes.AcademicLevel
import com.vanguard.classifiadmin.ui.screens.classes.JoinClassOption
import com.vanguard.classifiadmin.ui.screens.dashboard.DashboardBottomSheetFlavor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val store: PrefDatastore,
) : ViewModel() {

    private var _classCodeAddClass = MutableStateFlow(null as String?)
    val classCodeAddClass: StateFlow<String?> = _classCodeAddClass

    private var _selectedJoinClassOption = MutableStateFlow(null as JoinClassOption?)
    val selectedJoinClassOption: StateFlow<JoinClassOption?> = _selectedJoinClassOption

    private var _classNameAddClass = MutableStateFlow(null as String?)
    val classNameAddClass: StateFlow<String?> = _classNameAddClass

    private var _selectedAcademicLevelAddClass = MutableStateFlow(null as AcademicLevel?)
    val selectedAcademicLevelAddClass: StateFlow<AcademicLevel?> = _selectedAcademicLevelAddClass

    private var _selectedClassManageClass = MutableStateFlow(null as String?)
    val selectedClassManageClass: StateFlow<String?> = _selectedClassManageClass

    private var _currentDashboardBottomSheetFlavor =
        MutableStateFlow(null as DashboardBottomSheetFlavor?)
    val currentDashboardBottomSheetFlavor: StateFlow<DashboardBottomSheetFlavor?> =
        _currentDashboardBottomSheetFlavor


    private var _currentAssessmentOption = MutableStateFlow(null as AssessmentOption?)
    val currentAssessmentOption: StateFlow<AssessmentOption?> = _currentAssessmentOption

    private var _currentUserFirebase =
        MutableStateFlow(Resource.Loading<FirebaseUser?>() as Resource<FirebaseUser?>)
    val currentUserFirebase: StateFlow<Resource<FirebaseUser?>> = _currentUserFirebase

    private var _emailLogin = MutableStateFlow(null as String?)
    val emailLogin: StateFlow<String?> = _emailLogin

    private var _passwordLogin = MutableStateFlow(null as String?)
    val passwordLogin: StateFlow<String?> = _passwordLogin

    private var _fullNameCreateSchool = MutableStateFlow(null as String?)
    val fullNameCreateSchool: StateFlow<String?> = _fullNameCreateSchool

    private var _schoolNameCreateSchool = MutableStateFlow(null as String?)
    val schoolNameCreateSchool: StateFlow<String?> = _schoolNameCreateSchool

    private var _emailCreateSchool = MutableStateFlow(null as String?)
    val emailCreateSchool: StateFlow<String?> = _emailCreateSchool

    private var _phoneCreateSchool = MutableStateFlow(null as String?)
    val phoneCreateSchool: StateFlow<String?> = _phoneCreateSchool

    private var _passwordCreateSchool = MutableStateFlow(null as String?)
    val passwordCreateSchool: StateFlow<String?> = _passwordCreateSchool

    private var _selectedPhoneCode = MutableStateFlow(null as String?)
    val selectedPhoneCode: StateFlow<String?> = _selectedPhoneCode

    private var _userByIdNetwork =
        MutableStateFlow(Resource.Loading<UserNetworkModel?>() as Resource<UserNetworkModel?>)
    val userByIdNetwork: StateFlow<Resource<UserNetworkModel?>> = _userByIdNetwork

    private var _schoolByIdNetwork =
        MutableStateFlow(Resource.Loading<SchoolNetworkModel?>() as Resource<SchoolNetworkModel?>)
    val schoolByIdNetwork: StateFlow<Resource<SchoolNetworkModel?>> = _schoolByIdNetwork

    private var _userLoginState = MutableStateFlow(UserLoginState.Registered as UserLoginState)
    val userLoginState: StateFlow<UserLoginState> = _userLoginState

    private var _currentUserIdPref = MutableStateFlow(null as String?)
    val currentUserIdPref: StateFlow<String?> = _currentUserIdPref

    private var _currentUsernamePref = MutableStateFlow(null as String?)
    val currentUsernamePref: StateFlow<String?> = _currentUsernamePref

    private var _currentSchoolIdPref = MutableStateFlow(null as String?)
    val currentSchoolIdPref: StateFlow<String?> = _currentSchoolIdPref

    private var _currentSchoolNamePref = MutableStateFlow(null as String?)
    val currentSchoolNamePref: StateFlow<String?> = _currentSchoolNamePref

    fun getCurrentUserIdPref() = effect {
        store.currentUserIdPref.collect { id ->
            _currentUserIdPref.value = id
        }
    }

    fun getCurrentUsernamePref() = effect {
        store.currentUsernamePref.collect { name ->
            _currentUsernamePref.value = name
        }
    }

    fun getCurrentSchoolIdPref() = effect {
        store.currentSchoolIdPref.collect { id ->
            _currentSchoolIdPref.value = id
        }
    }

    fun getCurrentSchoolNamePref() = effect {
        store.currentSchoolNamePref.collect { name ->
            _currentSchoolNamePref.value = name
        }
    }

    fun saveCurrentUserIdPref(userId: String, onResult: (Boolean) -> Unit) = effect {
        store.saveCurrentUserIdPref(userId, onResult)
    }

    fun saveCurrentUsernamePref(username: String, onResult: (Boolean) -> Unit) = effect {
        store.saveCurrentUsernamePref(username, onResult)
    }

    fun saveCurrentSchoolIdPref(schoolId: String, onResult: (Boolean) -> Unit) = effect {
        store.saveCurrentSchoolIdPref(schoolId, onResult)
    }

    fun saveCurrentSchoolNamePref(schoolName: String, onResult: (Boolean) -> Unit) = effect {
        store.saveCurrentSchoolNamePref(schoolName, onResult)
    }


    fun onUserLoginStateChanged(state: UserLoginState) = effect {
        _userLoginState.value = state
    }

    //user
    fun saveUserNetwork(user: UserModel, onResult: (Boolean) -> Unit) = effect {
        repository.saveUserNetwork(user.toNetwork(), onResult)
    }

    fun getUserByIdNetwork(userId: String) = effect {
        repository.getUserByIdNetwork(userId) {
            _userByIdNetwork.value = it
        }
    }

    fun deleteUserByIdNetwork(userId: String, onResult: (Boolean) -> Unit) = effect {
        repository.deleteUserByIdNetwork(userId, onResult)
    }

    fun deleteUserNetwork(user: UserModel, onResult: (Boolean) -> Unit) = effect {
        repository.deleteUserNetwork(user.toNetwork(), onResult)
    }

    //school
    fun saveSchoolNetwork(school: SchoolModel, onResult: (Boolean) -> Unit) = effect {
        repository.saveSchoolNetwork(school.toNetwork(), onResult)
    }

    fun getSchoolByIdNetwork(schoolId: String) =
        effect {
            repository.getSchoolByIdNetwork(schoolId) {
                _schoolByIdNetwork.value = it
            }
        }

    fun deleteSchoolByIdNetwork(schoolId: String, onResult: (Boolean) -> Unit) = effect {
        repository.deleteSchoolByIdNetwork(schoolId, onResult)
    }

    fun deleteSchoolNetwork(school: SchoolModel, onResult: (Boolean) -> Unit) = effect {
        repository.deleteSchoolNetwork(school.toNetwork(), onResult)
    }

    fun onSelectedPhoneCodeChanged(code: String?) = effect {
        _selectedPhoneCode.value = code
    }

    fun onFullNameCreateSchoolChanged(name: String?) = effect {
        _fullNameCreateSchool.value = name
    }

    fun onSchoolNameCreateSchoolChanged(name: String?) = effect {
        _schoolNameCreateSchool.value = name
    }

    fun onEmailCreateSchoolChanged(email: String?) = effect {
        _emailCreateSchool.value = email
    }

    fun onPhoneCreateSchoolChanged(phone: String?) = effect {
        _phoneCreateSchool.value = phone
    }

    fun onPasswordCreateSchoolChanged(password: String?) = effect {
        _passwordCreateSchool.value = password
    }

    fun onEmailLoginChanged(email: String?) = effect {
        _emailLogin.value = email
    }

    fun onPasswordLoginChanged(password: String?) = effect {
        _passwordLogin.value = password
    }

    fun getCurrentUserFirebase() = effect {
        _currentUserFirebase.value = repository.currentUser
    }

    fun signUp(
        email: String?,
        password: String?,
        onResult: (Resource<FirebaseUser?>, Resource<AuthExceptionState?>) -> Unit
    ) = effect {
        repository.signUp(email, password, onResult)
        _fullNameCreateSchool.value = null
        _schoolNameCreateSchool.value = null
        _emailCreateSchool.value = null
        _phoneCreateSchool.value = null
        _passwordCreateSchool.value = null
    }

    fun signIn(
        email: String?,
        password: String?,
        onResult: (Resource<AuthExceptionState?>) -> Unit
    ) = effect {
        repository.signIn(email, password, onResult)
        _emailLogin.value = null
        _passwordLogin.value = null
    }

    fun signOut() = effect { repository.signOut() }

    fun onCurrentAssessmentOptionChanged(option: AssessmentOption?) = effect {
        _currentAssessmentOption.value = option
    }

    fun onCurrentDashboardBottomSheetFlavorChanged(flavor: DashboardBottomSheetFlavor?) = effect {
        _currentDashboardBottomSheetFlavor.value = flavor
    }

    fun onSelectedClassManageClassChanged(item: String?) = effect {
        _selectedClassManageClass.value = item
    }


    fun onClassNameAddClassChanged(name: String?) = effect {
        _classNameAddClass.value = name
    }

    fun onSelectedAcademicLevelAddClassChanged(level: AcademicLevel?) = effect {
        _selectedAcademicLevelAddClass.value = level
    }

    fun onSelectedJoinClassOptionChanged(option: JoinClassOption?) = effect {
        _selectedJoinClassOption.value = option
    }

    fun onClassCodeChanged(code: String?) = effect {
        _classCodeAddClass.value = code
    }


    private fun effect(block: suspend () -> Unit) = viewModelScope.launch { block() }
}