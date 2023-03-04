package com.vanguard.classifiadmin.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.vanguard.classifiadmin.data.local.models.ClassModel
import com.vanguard.classifiadmin.data.local.models.SchoolModel
import com.vanguard.classifiadmin.data.local.models.UserModel
import com.vanguard.classifiadmin.data.network.models.ClassNetworkModel
import com.vanguard.classifiadmin.data.network.models.SchoolNetworkModel
import com.vanguard.classifiadmin.data.network.models.SubjectNetworkModel
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
import com.vanguard.classifiadmin.ui.screens.profile.AccountBottomSheetState
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

    private var _userByEmailNetwork =
        MutableStateFlow(Resource.Loading<UserNetworkModel?>() as Resource<UserNetworkModel?>)
    val userByEmailNetwork: StateFlow<Resource<UserNetworkModel?>> = _userByEmailNetwork

    private var _currentUserEmailPref = MutableStateFlow(null as String?)
    val currentUserEmailPref: StateFlow<String?> = _currentUserEmailPref

    private var _usernameProfile = MutableStateFlow(null as String?)
    val usernameProfile: StateFlow<String?> = _usernameProfile

    private var _userPhoneProfile = MutableStateFlow(null as String?)
    val userPhoneProfile: StateFlow<String?> = _userPhoneProfile

    private var _userPasswordProfile = MutableStateFlow(null as String?)
    val userPasswordProfile: StateFlow<String?> = _userPasswordProfile

    private var _userBioProfile = MutableStateFlow(null as String?)
    val userBioProfile: StateFlow<String?> = _userBioProfile

    private var _userDobProfile = MutableStateFlow(null as String?)
    val userDobProfile: StateFlow<String?> = _userDobProfile

    private var _userAddressProfile = MutableStateFlow(null as String?)
    val userAddressProfile: StateFlow<String?> = _userAddressProfile

    private var _userCountryProfile = MutableStateFlow(null as String?)
    val userCountryProfile: StateFlow<String?> = _userCountryProfile

    private var _userStateProfile = MutableStateFlow(null as String?)
    val userStateProfile: StateFlow<String?> = _userStateProfile

    private var _userCityProfile = MutableStateFlow(null as String?)
    val userCityProfile: StateFlow<String?> = _userCityProfile

    private var _userPostalCodeProfile = MutableStateFlow(null as String?)
    val userPostalCodeProfile: StateFlow<String?> = _userPostalCodeProfile

    private var _accountBottomSheetState = MutableStateFlow(null as AccountBottomSheetState?)
    val accountBottomSheetState: StateFlow<AccountBottomSheetState?> = _accountBottomSheetState

    private var _avatarUri = MutableStateFlow(null as Uri?)
    val avatarUri: StateFlow<Uri?> = _avatarUri

    private var _currentProfileImagePref = MutableStateFlow(null as String?)
    val currentProfileImagePref: StateFlow<String?> = _currentProfileImagePref

    private var _classNameAdmin = MutableStateFlow(null as String?)
    val classNameAdmin: StateFlow<String?> = _classNameAdmin

    private var _classCodeAdmin = MutableStateFlow(null as String?)
    val classCodeAdmin: StateFlow<String?> = _classCodeAdmin

    private var _classByIdNetwork =
        MutableStateFlow(Resource.Loading<ClassNetworkModel?>() as Resource<ClassNetworkModel?>)
    val classByIdNetwork: StateFlow<Resource<ClassNetworkModel?>> = _classByIdNetwork

    private var _classByCodeNetwork =
        MutableStateFlow(Resource.Loading<ClassNetworkModel?>() as Resource<ClassNetworkModel?>)
    val classByCodeNetwork: StateFlow<Resource<ClassNetworkModel?>> = _classByCodeNetwork

    private var _verifiedClassesNetwork =
        MutableStateFlow(Resource.Loading<List<ClassNetworkModel>>() as Resource<List<ClassNetworkModel>>)
    val verifiedClassesNetwork: StateFlow<Resource<List<ClassNetworkModel>>> =
        _verifiedClassesNetwork

    private var _stagedClassesNetwork =
        MutableStateFlow(Resource.Loading<List<ClassNetworkModel>>() as Resource<List<ClassNetworkModel>>)
    val stagedClassesNetwork: StateFlow<Resource<List<ClassNetworkModel>>> = _stagedClassesNetwork

    private var _classAlreadyExistStateAdmin = MutableStateFlow(null as Boolean?)
    val classAlreadyExistStateAdmin: StateFlow<Boolean?> = _classAlreadyExistStateAdmin

    private var _subjectNameAdmin = MutableStateFlow(null as String?)
    val subjectNameAdmin: StateFlow<String?> = _subjectNameAdmin

    private var _subjectCodeAdmin = MutableStateFlow(null as String?)
    val subjectCodeAdmin: StateFlow<String?> = _subjectCodeAdmin

    private var _selectedClassSubjectCreationAdmin = MutableStateFlow(null as ClassModel?)
    val selectedClassSubjectCreationAdmin: StateFlow<ClassModel?> =
        _selectedClassSubjectCreationAdmin


    private var _subjectByIdNetwork =
        MutableStateFlow(Resource.Loading<SubjectNetworkModel?>() as Resource<SubjectNetworkModel?>)
    val subjectByIdNetwork: StateFlow<Resource<SubjectNetworkModel?>> = _subjectByIdNetwork

    private var _subjectByCodeNetwork =
        MutableStateFlow(Resource.Loading<SubjectNetworkModel?>() as Resource<SubjectNetworkModel?>)
    val subjectByCodeNetwork: StateFlow<Resource<SubjectNetworkModel?>> = _subjectByCodeNetwork

    private var _verifiedSubjectsNetwork =
        MutableStateFlow(Resource.Loading<List<SubjectNetworkModel>>() as Resource<List<SubjectNetworkModel>>)
    val verifiedSubjectsNetwork: StateFlow<Resource<List<SubjectNetworkModel>>> =
        _verifiedSubjectsNetwork

    private var _stagedSubjectsNetwork =
        MutableStateFlow(Resource.Loading<List<SubjectNetworkModel>>() as Resource<List<SubjectNetworkModel>>)
    val stagedSubjectsNetwork: StateFlow<Resource<List<SubjectNetworkModel>>> =
        _stagedSubjectsNetwork


    private var _subjectAlreadyExistStateAdmin = MutableStateFlow(null as Boolean?)
    val subjectAlreadyExistStateAdmin: StateFlow<Boolean?> = _subjectAlreadyExistStateAdmin

    fun onSubjectAlreadyExistStateAdminChanged(state: Boolean?) = effect {
        _subjectAlreadyExistStateAdmin.value = state
    }


    //subject
    fun saveSubjectAsStagedNetwork(
        subject: SubjectNetworkModel,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.saveSubjectAsStagedNetwork(subject, onResult)
    }

    fun saveSubjectsAsStagedNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.saveSubjectsAsStagedNetwork(subjects, onResult)
    }

    fun saveSubjectAsVerifiedNetwork(subject: SubjectNetworkModel, onResult: (Boolean) -> Unit) =
        effect {
            repository.saveSubjectAsVerifiedNetwork(subject, onResult)
        }

    fun saveSubjectsAsVerifiedNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.saveSubjectsAsVerifiedNetwork(subjects, onResult)
    }

    fun getSubjectByIdNetwork(
        subjectId: String,
        schoolId: String,
    ) = effect {
        repository.getSubjectByIdNetwork(subjectId, schoolId) {
            _subjectByIdNetwork.value = it
        }
    }

    fun getSubjectByCodeNetwork(
        code: String,
        schoolId: String,
    ) = effect {
        repository.getSubjectByCodeNetwork(code, schoolId) {
            _subjectByCodeNetwork.value = it
        }
    }

    fun getVerifiedSubjectsNetwork(
        schoolId: String,
    ) = effect {
        repository.getVerifiedSubjectsNetwork(schoolId) {
            _verifiedSubjectsNetwork.value = it
        }
    }

    fun getStagedSubjectsNetwork(
        schoolId: String,
    ) = effect {
        repository.getStagedSubjectsNetwork(schoolId) {
            _stagedSubjectsNetwork.value = it
        }
    }

    fun deleteSubjectNetwork(
        subject: SubjectNetworkModel,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.deleteSubjectNetwork(subject, onResult)
    }

    fun deleteSubjectsNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.deleteSubjectsNetwork(subjects, onResult)
    }

    fun onSelectedClassSubjectCreationAdminChanged(myClass: ClassModel?) = effect {
        _selectedClassSubjectCreationAdmin.value = myClass
    }

    fun onSubjectNameAdminChanged(name: String?) = effect {
        _subjectNameAdmin.value = name
    }

    fun onSubjectCodeAdminChanged(code: String?) = effect {
        _subjectCodeAdmin.value = code
    }

    fun onClassAlreadyExistStateAdminChanged(state: Boolean?) = effect {
        _classAlreadyExistStateAdmin.value = state
    }


    fun clearCreateClassAdminFields() = effect {
        _classNameAdmin.value = null
        _classCodeAdmin.value = null
    }

    fun clearCreateSubjectAdminFields() = effect {
        _subjectCodeAdmin.value = null
        _subjectNameAdmin.value = null
        _selectedClassSubjectCreationAdmin.value = null
    }

    //class
    fun saveClassAsStagedNetwork(
        myClass: ClassNetworkModel,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.saveClassAsStagedNetwork(myClass, onResult)
    }

    fun saveClassesAsStagedNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.saveClassesAsStagedNetwork(myClasses, onResult)
    }


    fun saveClassAsVerifiedNetwork(myClass: ClassNetworkModel, onResult: (Boolean) -> Unit) =
        effect {
            repository.saveClassAsVerifiedNetwork(myClass, onResult)
        }

    fun saveClassesAsVerifiedNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.saveClassesAsVerifiedNetwork(myClasses, onResult)
    }

    fun getClassByIdNetwork(
        classId: String,
        schoolId: String,
    ) = effect {
        repository.getClassByIdNetwork(classId, schoolId) {
            _classByIdNetwork.value = it
        }
    }

    fun getClassByCodeNetwork(
        code: String,
        schoolId: String,
    ) = effect {
        repository.getClassByCodeNetwork(code, schoolId) {
            _classByCodeNetwork.value = it
        }
    }

    fun getVerifiedClassesNetwork(
        schoolId: String,
    ) = effect {
        repository.getVerifiedClassesNetwork(schoolId) {
            _verifiedClassesNetwork.value = it
        }
    }

    fun getStagedClassesNetwork(
        schoolId: String,
    ) = effect {
        repository.getStagedClassesNetwork(schoolId) {
            _stagedClassesNetwork.value = it
        }
    }

    fun deleteClassNetwork(
        myClass: ClassNetworkModel,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.deleteClassNetwork(myClass, onResult)
    }

    fun deleteClassesNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.deleteClassesNetwork(myClasses, onResult)
    }

    fun onClassNameAdminChanged(name: String?) = effect {
        _classNameAdmin.value = name
    }

    fun onClassCodeAdminChanged(code: String?) = effect {
        _classCodeAdmin.value = code
    }

    fun getCurrentProfileImagePref() = effect {
        store.currentProfileImagePref.collect { url ->
            _currentProfileImagePref.value = url
        }
    }

    fun saveCurrentProfileImage(downloadUrl: String, onResult: (Boolean) -> Unit) = effect {
        store.saveCurrentProfileImagePref(downloadUrl, onResult)
    }

    fun onAvatarUriChanged(uri: Uri?) = effect {
        _avatarUri.value = uri
    }

    fun uploadAvatar(
        fileUri: Uri,
        userId: String,
        onProgress: (Long, Long) -> Unit,
        onResult: (Boolean, String) -> Unit
    ) = effect {
        repository.uploadAvatar(fileUri, userId, onProgress, onResult)
    }

    fun onAccountBottomSheetStateChanged(state: AccountBottomSheetState?) = effect {
        _accountBottomSheetState.value = state
    }

    fun onUserPostalCodeProfileChanged(postal: String?) = effect {
        _userPostalCodeProfile.value = postal
    }

    fun onUserCityProfileChanged(city: String?) = effect {
        _userCityProfile.value = city
    }

    fun onUserStateProfileChanged(state: String?) = effect {
        _userStateProfile.value = state
    }

    fun onUserCountryProfileChanged(country: String?) = effect {
        _userCountryProfile.value = country
    }

    fun onUserAddressProfileChanged(address: String?) = effect {
        _userAddressProfile.value = address
    }

    fun onUserDobProfileChanged(dob: String?) = effect {
        _userDobProfile.value = dob
    }

    fun onUserBioProfileChanged(bio: String?) = effect {
        _userBioProfile.value = bio
    }

    fun onUserPasswordProfileChanged(password: String?) = effect {
        _userPasswordProfile.value = password
    }

    fun onUserPhoneProfileChanged(phone: String?) = effect {
        _userPhoneProfile.value = phone
    }

    fun onUsernameProfileChanged(name: String?) = effect {
        _usernameProfile.value = name
    }

    fun saveCurrentUserEmailPref(email: String, onResult: (Boolean) -> Unit) = effect {
        store.saveCurrentUserEmailPref(email, onResult)
    }

    fun getCurrentUserEmail() = effect {
        store.currentUserEmailPref.collect { email ->
            _currentUserEmailPref.value = email
        }
    }

    fun getUserByEmailNetwork(email: String, onResult: (Resource<UserNetworkModel?>) -> Unit) =
        effect {
            repository.getUserByEmailNetwork(email) {
                _userByEmailNetwork.value = it
            }
        }

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

    }

    fun signIn(
        email: String?,
        password: String?,
        onResult: (Resource<AuthExceptionState?>) -> Unit
    ) = effect {
        repository.signIn(email, password, onResult)
    }

    fun clearSignInFields() = effect {
        _emailLogin.value = null
        _passwordLogin.value = null
    }

    fun clearSignUpFields() = effect {
        _fullNameCreateSchool.value = null
        _schoolNameCreateSchool.value = null
        _emailCreateSchool.value = null
        _phoneCreateSchool.value = null
        _passwordCreateSchool.value = null
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