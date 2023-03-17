package com.vanguard.classifiadmin.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.vanguard.classifiadmin.data.local.models.ClassModel
import com.vanguard.classifiadmin.data.local.models.SchoolModel
import com.vanguard.classifiadmin.data.local.models.SubjectModel
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
import com.vanguard.classifiadmin.ui.screens.admin.EnrollParentException
import com.vanguard.classifiadmin.ui.screens.admin.EnrollStudentException
import com.vanguard.classifiadmin.ui.screens.admin.EnrollTeacherException
import com.vanguard.classifiadmin.ui.screens.admin.ManageClassAdminDetailFeature
import com.vanguard.classifiadmin.ui.screens.admin.ManageClassAdminDetailMessage
import com.vanguard.classifiadmin.ui.screens.admin.ManageClassMessage
import com.vanguard.classifiadmin.ui.screens.admin.ManageClassSubsectionItem
import com.vanguard.classifiadmin.ui.screens.admin.ManageSubjectMessage
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentOption
import com.vanguard.classifiadmin.ui.screens.classes.AcademicLevel
import com.vanguard.classifiadmin.ui.screens.classes.JoinClassOption
import com.vanguard.classifiadmin.ui.screens.dashboard.DashboardBottomSheetFlavor
import com.vanguard.classifiadmin.ui.screens.profile.AccountBottomSheetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val store: PrefDatastore,
) : ViewModel() {
    val TAG = "MainViewModel"

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

    private var _teacherEmailEnrollTeacher = MutableStateFlow(null as String?)
    val teacherEmailEnrollTeacher: StateFlow<String?> = _teacherEmailEnrollTeacher

    private var _teacherPasswordEnrollTeacher = MutableStateFlow(null as String?)
    val teacherPasswordEnrollTeacher: StateFlow<String?> = _teacherPasswordEnrollTeacher

    private var _teacherConfirmPasswordEnrollTeacher = MutableStateFlow(null as String?)
    val teacherConfirmPasswordEnrollTeacher: StateFlow<String?> =
        _teacherConfirmPasswordEnrollTeacher

    private var _teacherAlreadyExistStateAdmin = MutableStateFlow(null as Boolean?)
    val teacherAlreadyExistStateAdmin: StateFlow<Boolean?> = _teacherAlreadyExistStateAdmin

    private var _verifiedUsersNetwork =
        MutableStateFlow(Resource.Loading<List<UserNetworkModel>>() as Resource<List<UserNetworkModel>>)
    val verifiedUsersNetwork: StateFlow<Resource<List<UserNetworkModel>>> = _verifiedUsersNetwork

    private var _stagedUsersNetwork =
        MutableStateFlow(Resource.Loading<List<UserNetworkModel>>() as Resource<List<UserNetworkModel>>)
    val stagedUsersNetwork: StateFlow<Resource<List<UserNetworkModel>>> = _stagedUsersNetwork

    private var _currentPageMyAccountScreen = MutableStateFlow(null as Int?)
    val currentPageMyAccountScreen: StateFlow<Int?> = _currentPageMyAccountScreen

    private var _enrollTeacherException =
        MutableStateFlow(EnrollTeacherException.NoException as EnrollTeacherException)
    val enrollTeacherException: StateFlow<EnrollTeacherException> = _enrollTeacherException

    private var _enrollStudentException =
        MutableStateFlow(EnrollStudentException.NoException as EnrollStudentException)
    val enrollStudentException: StateFlow<EnrollStudentException> = _enrollStudentException

    private var _studentEmailEnrollStudent = MutableStateFlow(null as String?)
    val studentEmailEnrollStudent: StateFlow<String?> = _studentEmailEnrollStudent

    private var _studentPasswordEnrollStudent = MutableStateFlow(null as String?)
    val studentPasswordEnrollStudent: StateFlow<String?> = _studentPasswordEnrollStudent

    private var _studentConfirmPasswordEnrollStudent = MutableStateFlow(null as String?)
    val studentConfirmPasswordEnrollStudent: StateFlow<String?> =
        _studentConfirmPasswordEnrollStudent

    private var _studentAlreadyExistStateAdmin = MutableStateFlow(null as Boolean?)
    val studentAlreadyExistStateAdmin: StateFlow<Boolean?> = _studentAlreadyExistStateAdmin

    private var _stagedStudentsNetwork =
        MutableStateFlow(Resource.Loading<List<UserNetworkModel>>() as Resource<List<UserNetworkModel>>)
    val stagedStudentsNetwork: StateFlow<Resource<List<UserNetworkModel>>> = _stagedStudentsNetwork

    private var _stagedTeachersNetwork =
        MutableStateFlow(Resource.Loading<List<UserNetworkModel>>() as Resource<List<UserNetworkModel>>)
    val stagedTeachersNetwork: StateFlow<Resource<List<UserNetworkModel>>> = _stagedTeachersNetwork

    private var _stagedParentsNetwork =
        MutableStateFlow(Resource.Loading<List<UserNetworkModel>>() as Resource<List<UserNetworkModel>>)
    val stagedParentsNetwork: StateFlow<Resource<List<UserNetworkModel>>> = _stagedParentsNetwork

    private var _verifiedStudentsNetwork =
        MutableStateFlow(Resource.Loading<List<UserNetworkModel>>() as Resource<List<UserNetworkModel>>)
    val verifiedStudentsNetwork: StateFlow<Resource<List<UserNetworkModel>>> =
        _verifiedStudentsNetwork

    private var _verifiedTeachersNetwork =
        MutableStateFlow(Resource.Loading<List<UserNetworkModel>>() as Resource<List<UserNetworkModel>>)
    val verifiedTeachersNetwork: StateFlow<Resource<List<UserNetworkModel>>> =
        _verifiedTeachersNetwork

    private var _verifiedParentsNetwork =
        MutableStateFlow(Resource.Loading<List<UserNetworkModel>>() as Resource<List<UserNetworkModel>>)
    val verifiedParentsNetwork: StateFlow<Resource<List<UserNetworkModel>>> =
        _verifiedParentsNetwork

    private var _parentEmailEnrollParent = MutableStateFlow(null as String?)
    val parentEmailEnrollParent: StateFlow<String?> = _parentEmailEnrollParent

    private var _parentPasswordEnrollParent = MutableStateFlow(null as String?)
    val parentPasswordEnrollParent: StateFlow<String?> = _parentPasswordEnrollParent

    private var _parentConfirmPasswordEnrollParent = MutableStateFlow(null as String?)
    val parentConfirmPasswordEnrollParent: StateFlow<String?> = _parentConfirmPasswordEnrollParent

    private var _enrollParentException =
        MutableStateFlow(EnrollParentException.NoException as EnrollParentException)
    val enrollParentException: StateFlow<EnrollParentException> = _enrollParentException

    private var _parentAlreadyExistStateAdmin = MutableStateFlow(null as Boolean?)
    val parentAlreadyExistStateAdmin: StateFlow<Boolean?> = _parentAlreadyExistStateAdmin

    private var _classSelectedStateManageClasses = MutableStateFlow(false as Boolean?)
    var classSelectedStateManageClasses: StateFlow<Boolean?> = _classSelectedStateManageClasses

    private var _selectedClassesManageClasses = MutableStateFlow(mutableListOf<String>())
    val selectedClassesManageClasses: StateFlow<List<String>> = _selectedClassesManageClasses

    private var _selectionListenerManageClass = MutableStateFlow(0)
    val selectionListenerManageClass: StateFlow<Int> = _selectionListenerManageClass

    private var _manageClassMessage =
        MutableStateFlow(ManageClassMessage.NoMessage as ManageClassMessage)
    val manageClassMessage: StateFlow<ManageClassMessage> = _manageClassMessage

    private var _selectedClassManageClassAdmin = MutableStateFlow(null as ClassModel?)
    val selectedClassManageClassAdmin: StateFlow<ClassModel?> = _selectedClassManageClassAdmin

    private var _selectedManageClassSubsectionItem =
        MutableStateFlow(ManageClassSubsectionItem.Students as ManageClassSubsectionItem)
    val selectedManageClassSubsectionItem: StateFlow<ManageClassSubsectionItem> =
        _selectedManageClassSubsectionItem

    private var _verifiedStudentsUnderClassNetwork =
        MutableStateFlow(Resource.Loading<List<UserNetworkModel>>() as Resource<List<UserNetworkModel>>)
    val verifiedStudentsUnderClassNetwork: StateFlow<Resource<List<UserNetworkModel>>> =
        _verifiedStudentsUnderClassNetwork

    private var _verifiedTeachersUnderClassNetwork =
        MutableStateFlow(Resource.Loading<List<UserNetworkModel>>() as Resource<List<UserNetworkModel>>)
    val verifiedTeachersUnderClassNetwork: StateFlow<Resource<List<UserNetworkModel>>> =
        _verifiedTeachersUnderClassNetwork

    private var _verifiedSubjectsUnderClassNetwork =
        MutableStateFlow(Resource.Loading<List<SubjectNetworkModel>>() as Resource<List<SubjectNetworkModel>>)
    val verifiedSubjectsUnderClassNetwork: StateFlow<Resource<List<SubjectNetworkModel>>> =
        _verifiedSubjectsUnderClassNetwork

    private var _importStudentBuffer = MutableStateFlow(mutableListOf<String>())
    val importStudentBuffer: StateFlow<List<String>> = _importStudentBuffer

    private var _importStudentBufferListener = MutableStateFlow(0)
    val importStudentBufferListener: StateFlow<Int> = _importStudentBufferListener

    private var _importSubjectBuffer = MutableStateFlow(mutableListOf<String>())
    val importSubjectBuffer: StateFlow<List<String>> = _importSubjectBuffer

    private var _importSubjectBufferListener = MutableStateFlow(0)
    val importSubjectBufferListener: StateFlow<Int> = _importSubjectBufferListener


    private var _manageClassAdminDetailListener = MutableStateFlow(0)
    val manageClassAdminDetailListener: StateFlow<Int> = _manageClassAdminDetailListener

    private var _manageClassAdminDetailFeature =
        MutableStateFlow(ManageClassAdminDetailFeature.NoFeature as ManageClassAdminDetailFeature)
    val manageClassAdminDetailFeature: StateFlow<ManageClassAdminDetailFeature> =
        _manageClassAdminDetailFeature

    private var _importTeacherBufferListener = MutableStateFlow(0)
    val importTeacherBufferListener: StateFlow<Int> = _importTeacherBufferListener

    private var _importTeacherBuffer = MutableStateFlow(mutableListOf<String>())
    val importTeacherBuffer: StateFlow<List<String>> = _importTeacherBuffer

    private var _manageClassAdminDetailTeacherBuffer = MutableStateFlow(mutableListOf<String>())
    val manageClassAdminDetailTeacherBuffer: StateFlow<List<String>> =
        _manageClassAdminDetailTeacherBuffer

    private var _manageClassAdminDetailStudentBuffer = MutableStateFlow(mutableListOf<String>())
    val manageClassAdminDetailStudentBuffer: StateFlow<List<String>> =
        _manageClassAdminDetailStudentBuffer

    private var _manageClassAdminDetailSubjectBuffer = MutableStateFlow(mutableListOf<String>())
    val manageClassAdminDetailSubjectBuffer: StateFlow<List<String>> =
        _manageClassAdminDetailSubjectBuffer

    private var _manageClassAdminDetailTeacherBufferListener = MutableStateFlow(0)
    val manageClassAdminDetailTeacherBufferListener: StateFlow<Int> =
        _manageClassAdminDetailTeacherBufferListener

    private var _manageClassAdminDetailStudentBufferListener = MutableStateFlow(0)
    val manageClassAdminDetailStudentBufferListener: StateFlow<Int> =
        _manageClassAdminDetailStudentBufferListener

    private var _manageClassAdminDetailSubjectBufferListener = MutableStateFlow(0)
    val manageClassAdminDetailSubjectBufferListener: StateFlow<Int> =
        _manageClassAdminDetailSubjectBufferListener

    private var _exportTeacherBuffer = MutableStateFlow(mutableListOf<String>())
    val exportTeacherBuffer: StateFlow<List<String>> = _exportTeacherBuffer

    private var _exportTeacherBufferListener = MutableStateFlow(0)
    val exportTeacherBufferListener: StateFlow<Int> = _exportTeacherBufferListener

    private var _manageClassAdminDetailMessage =
        MutableStateFlow(ManageClassAdminDetailMessage.NoMessage as ManageClassAdminDetailMessage)
    val manageClassAdminDetailMessage: StateFlow<ManageClassAdminDetailMessage> =
        _manageClassAdminDetailMessage

    private var _exportSubjectBuffer = MutableStateFlow(mutableListOf<String>())
    val exportSubjectBuffer: StateFlow<List<String>> = _exportSubjectBuffer

    private var _exportSubjectBufferListener = MutableStateFlow(0)
    val exportSubjectBufferListener: StateFlow<Int> = _exportSubjectBufferListener

    private var _exportStudentBuffer = MutableStateFlow(mutableListOf<String>())
    val exportStudentBuffer: StateFlow<List<String>> = _exportStudentBuffer

    private var _exportStudentBufferListener = MutableStateFlow(0)
    val exportStudentBufferListener: StateFlow<Int> = _exportStudentBufferListener

    private var _selectedSubjectManageSubjectAdminBuffer = MutableStateFlow(mutableListOf<String>())
    val selectedSubjectManageSubjectAdminBuffer: StateFlow<List<String>> = _selectedSubjectManageSubjectAdminBuffer

    private var _subjectSelectionListenerManageSubject = MutableStateFlow(0)
    val subjectSelectionListenerManageSubject: StateFlow<Int> = _subjectSelectionListenerManageSubject

    private var _selectedSubjectManageSubjectAdmin = MutableStateFlow(null as SubjectModel?)
    val selectedSubjectManageSubjectAdmin: StateFlow<SubjectModel?> =
        _selectedSubjectManageSubjectAdmin

    private var _manageSubjectMessage =
        MutableStateFlow(ManageSubjectMessage.NoMessage as ManageSubjectMessage)
    val manageSubjectMessage: StateFlow<ManageSubjectMessage> = _manageSubjectMessage

    fun onManageSubjectMessageChanged(message: ManageSubjectMessage) = effect {
        _manageSubjectMessage.value = message
    }

    fun onSelectedSubjectManageSubjectAdminChanged(subject: SubjectModel?) = effect {
        _selectedSubjectManageSubjectAdmin.value = subject
    }

    fun onIncSubjectSelectionListenerManageSubject() = effect {
        _subjectSelectionListenerManageSubject.value++
    }

    fun onDecSubjectSelectionListenerManageSubject() = effect {
        _subjectSelectionListenerManageSubject.value--
    }

    fun onAddToSelectedSubjectManageSubjectBuffer(subjectId: String) = effect {
        if (!_selectedSubjectManageSubjectAdminBuffer.value.contains(subjectId)) {
            _selectedSubjectManageSubjectAdminBuffer.value.add(subjectId)
        }
    }

    fun onRemoveFromSelectedSubjectManageSubjectBuffer(subjectId: String) = effect {
        if (_selectedSubjectManageSubjectAdminBuffer.value.contains(subjectId)) {
            _selectedSubjectManageSubjectAdminBuffer.value.remove(subjectId)
        }
    }

    fun clearSelectedSubjectManageSubjectBuffer() = effect {
        _selectedSubjectManageSubjectAdminBuffer.value.clear()
    }


    fun onIncExportStudentBufferListener() = effect {
        _exportStudentBufferListener.value++
    }

    fun onDecExportStudentBufferListener() = effect {
        _exportStudentBufferListener.value--
    }

    fun onAddToExportStudentBuffer(classId: String) = effect {
        if (!_exportStudentBuffer.value.contains(classId)) {
            _exportStudentBuffer.value.add(classId)
        }
    }

    fun onRemoveFromExportStudentBuffer(classId: String) = effect {
        if (_exportStudentBuffer.value.contains(classId)) {
            _exportStudentBuffer.value.remove(classId)
        }
    }

    fun clearExportStudentBuffer() = effect {
        _exportStudentBuffer.value.clear()
    }

    fun onIncExportSubjectBufferListener() = effect {
        _exportSubjectBufferListener.value++
    }

    fun onDecExportSubjectBufferListener() = effect {
        _exportSubjectBufferListener.value--
    }

    fun onAddToExportSubjectBuffer(classId: String) = effect {
        if (!_exportSubjectBuffer.value.contains(classId)) {
            _exportSubjectBuffer.value.add(classId)
        }
    }

    fun onRemoveFromExportSubjectBuffer(classId: String) = effect {
        if (_exportSubjectBuffer.value.contains(classId)) {
            _exportSubjectBuffer.value.remove(classId)
        }
    }

    fun clearExportSubjectBuffer() = effect {
        _exportSubjectBuffer.value.clear()
    }

    fun onManageClassAdminDetailMessageChanged(message: ManageClassAdminDetailMessage) = effect {
        _manageClassAdminDetailMessage.value = message
    }

    fun onIncExportTeacherBufferListener() = effect {
        _exportTeacherBufferListener.value++
    }

    fun onDecExportTeacherBufferListener() = effect {
        _exportTeacherBufferListener.value--
    }

    fun onAddToExportTeacherBuffer(classId: String) = effect {
        if (!_exportTeacherBuffer.value.contains(classId)) {
            _exportTeacherBuffer.value.add(classId)
        }
    }

    fun onRemoveFromExportTeacherBuffer(classId: String) = effect {
        if (_exportTeacherBuffer.value.contains(classId)) {
            _exportTeacherBuffer.value.remove(classId)
        }
    }

    fun clearExportTeacherBuffer() = effect {
        _exportTeacherBuffer.value.clear()
    }

    fun onIncManageClassAdminDetailSubjectBufferListener() = effect {
        _manageClassAdminDetailSubjectBufferListener.value++
    }

    fun onDecManageClassAdminDetailSubjectBufferListener() = effect {
        _manageClassAdminDetailSubjectBufferListener.value--
    }

    fun onIncManageClassAdminDetailTeacherBufferListener() = effect {
        _manageClassAdminDetailTeacherBufferListener.value++
    }

    fun onDecManageClassAdminDetailTeacherBufferListener() = effect {
        _manageClassAdminDetailTeacherBufferListener.value--
    }

    fun onIncManageClassAdminDetailStudentBufferListener() = effect {
        _manageClassAdminDetailStudentBufferListener.value++
    }

    fun onDecManageClassAdminDetailStudentBufferListener() = effect {
        _manageClassAdminDetailStudentBufferListener.value--
    }

    fun onAddToTeacherBufferManageClassAdminDetail(id: String) = effect {
        if (!_manageClassAdminDetailTeacherBuffer.value.contains(id)) {
            _manageClassAdminDetailTeacherBuffer.value.add(id)
        }
    }

    fun onRemoveFromTeacherBufferManageClassAdminDetail(id: String) = effect {
        if (_manageClassAdminDetailTeacherBuffer.value.contains(id)) {
            _manageClassAdminDetailTeacherBuffer.value.remove(id)
        }
    }

    fun clearTeacherBufferManageClassAdminDetail() = effect {
        _manageClassAdminDetailTeacherBuffer.value.clear()
    }

    fun clearStudentBufferManageClassAdminDetail() = effect {
        _manageClassAdminDetailStudentBuffer.value.clear()
    }

    fun clearSubjectBufferManageClassAdminDetail() = effect {
        _manageClassAdminDetailSubjectBuffer.value.clear()
    }

    fun onAddToStudentBufferManageClassAdminDetail(id: String) = effect {
        if (!_manageClassAdminDetailStudentBuffer.value.contains(id)) {
            _manageClassAdminDetailStudentBuffer.value.add(id)
        }
    }

    fun onRemoveFromStudentBufferManageClassAdminDetail(id: String) = effect {
        if (_manageClassAdminDetailStudentBuffer.value.contains(id)) {
            _manageClassAdminDetailStudentBuffer.value.remove(id)
        }
    }

    fun onAddToSubjectBufferManageClassAdminDetail(id: String) = effect {
        if (!_manageClassAdminDetailSubjectBuffer.value.contains(id)) {
            _manageClassAdminDetailSubjectBuffer.value.add(id)
        }
    }

    fun onRemoveFromSubjectBufferManageClassAdminDetail(id: String) = effect {
        if (_manageClassAdminDetailSubjectBuffer.value.contains(id)) {
            _manageClassAdminDetailSubjectBuffer.value.remove(id)
        }
    }


    fun onAddToImportTeacherBuffer(id: String) = effect {
        if (!_importTeacherBuffer.value.contains(id)) {
            _importTeacherBuffer.value.add(id)
        }
    }

    fun onRemoveFromImportTeacherBuffer(id: String) = effect {
        if (_importTeacherBuffer.value.contains(id)) {
            _importTeacherBuffer.value.remove(id)
        }
    }

    fun clearImportTeacherBuffer() = effect {
        _importTeacherBuffer.value.clear()
    }

    fun onIncImportTeacherBufferListener() = effect {
        _importTeacherBufferListener.value++
    }

    fun onDecImportTeacherBufferListener() = effect {
        _importTeacherBufferListener.value--
    }

    fun onAddToImportSubjectBuffer(id: String) = effect {
        if (!_importSubjectBuffer.value.contains(id)) {
            _importSubjectBuffer.value.add(id)
        }
    }

    fun onRemoveFromImportSubjectBuffer(id: String) = effect {
        if (_importSubjectBuffer.value.contains(id)) {
            _importSubjectBuffer.value.remove(id)
        }
    }

    fun clearImportSubjectBuffer() = effect {
        _importSubjectBuffer.value.clear()
    }

    fun onIncImportSubjectBufferListener() = effect {
        _importSubjectBufferListener.value++
    }

    fun onDecImportSubjectBufferListener() = effect {
        _importSubjectBufferListener.value--
    }

    fun onManageClassAdminDetailFeatureChanged(feature: ManageClassAdminDetailFeature) = effect {
        _manageClassAdminDetailFeature.value = feature
    }

    fun onIncManageClassAdminDetailListener() = effect {
        _manageClassAdminDetailListener.value++
    }

    fun onDecManageClassAdminDetailListener() = effect {
        _manageClassAdminDetailListener.value--
    }

    fun onAddToImportStudentBuffer(id: String) = effect {
        if (!_importStudentBuffer.value.contains(id)) {
            _importStudentBuffer.value.add(id)
        }
    }

    fun onRemoveFromImportStudentBuffer(id: String) = effect {
        if (_importStudentBuffer.value.contains(id)) {
            _importStudentBuffer.value.remove(id)
        }
    }

    fun clearImportStudentBuffer() = effect {
        _importStudentBuffer.value.clear()
    }

    fun onIncImportStudentBufferListener() = effect {
        _importStudentBufferListener.value++
    }

    fun onDecImportStudentBufferListener() = effect {
        _importStudentBufferListener.value--
    }

    fun getVerifiedStudentsUnderClassNetwork(
        classId: String,
        schoolId: String,
    ) = effect {
        repository.getVerifiedStudentsUnderClassNetwork(classId, schoolId) {
            _verifiedStudentsUnderClassNetwork.value = it
        }
    }

    fun getVerifiedTeachersUnderClassNetwork(
        classId: String,
        schoolId: String,
    ) = effect {
        repository.getVerifiedTeachersUnderClassNetwork(classId, schoolId) {
            _verifiedTeachersUnderClassNetwork.value = it
        }
    }


    fun getVerifiedSubjectsUnderClassNetwork(
        classId: String,
        schoolId: String,
    ) = effect {
        repository.getVerifiedSubjectsUnderClassNetwork(classId, schoolId) {
            _verifiedSubjectsUnderClassNetwork.value = it
        }
    }


    fun onSelectedManageClassSubsectionItemChanged(item: ManageClassSubsectionItem) = effect {
        _selectedManageClassSubsectionItem.value = item
    }

    fun onSelectedClassManageClassAdminChanged(selected: ClassModel?) = effect {
        _selectedClassManageClassAdmin.value = selected
    }

    fun onManageClassMessageChanged(message: ManageClassMessage) = effect {
        _manageClassMessage.value = message
    }

    fun onIncSelectionListenerManageClass() = effect {
        _selectionListenerManageClass.value++
    }

    fun onDecSelectionListenerManageClass() = effect {
        _selectionListenerManageClass.value--
    }

    fun onAddClassToBuffer(code: String) = effect {
        if (!_selectedClassesManageClasses.value.contains(code)) {
            _selectedClassesManageClasses.value.add(code)
        }
    }

    fun onRemoveClassFromBuffer(code: String) = effect {
        if (_selectedClassesManageClasses.value.contains(code)) {
            _selectedClassesManageClasses.value.remove(code)
        }
    }

    fun onClearBufferManageClass() = effect {
        _selectedClassesManageClasses.value.clear()
    }

    fun onClassSelectedStateManageClassesChanged(state: Boolean?) = effect {
        _classSelectedStateManageClasses.value = state
    }

    fun onParentAlreadyExistStateAdminChanged(state: Boolean?) = effect {
        _parentAlreadyExistStateAdmin.value = state
    }

    fun onEnrollParentExceptionChanged(exception: EnrollParentException) = effect {
        _enrollParentException.value = exception
    }

    fun onParentEmailEnrollParentChanged(email: String?) = effect {
        _parentEmailEnrollParent.value = email
    }

    fun onParentPasswordEnrollParentChanged(password: String?) = effect {
        _parentPasswordEnrollParent.value = password
    }

    fun onParentConfirmPasswordEnrollParentChanged(confirm: String?) = effect {
        _parentConfirmPasswordEnrollParent.value = confirm
    }

    fun getVerifiedTeachersNetwork(
        schoolId: String,
    ) = effect {
        repository.getVerifiedTeachersNetwork(schoolId) {
            _verifiedTeachersNetwork.value = it
        }
    }

    fun getStagedTeachersNetwork(
        schoolId: String,
    ) = effect {
        repository.getStagedTeachersNetwork(schoolId) {
            _stagedTeachersNetwork.value = it
        }
    }

    fun getVerifiedStudentsNetwork(
        schoolId: String,
    ) = effect {
        repository.getVerifiedStudentsNetwork(schoolId) {
            _verifiedStudentsNetwork.value = it
        }
    }

    fun getStagedStudentsNetwork(
        schoolId: String,
    ) = effect {
        repository.getStagedStudentsNetwork(schoolId) {
            _stagedStudentsNetwork.value = it
        }
    }

    fun getVerifiedParentsNetwork(
        schoolId: String,
    ) = effect {
        repository.getVerifiedParentsNetwork(schoolId) {
            _verifiedParentsNetwork.value = it
        }
    }

    fun getStagedParentsNetwork(
        schoolId: String,
    ) = effect {
        repository.getStagedParentsNetwork(schoolId) {
            _stagedParentsNetwork.value = it
        }
    }

    fun onStudentAlreadyExistStateAdminChanged(state: Boolean?) = effect {
        _studentAlreadyExistStateAdmin.value = state
    }

    fun onStudentEmailEnrollStudentChanged(email: String?) = effect {
        _studentEmailEnrollStudent.value = email
    }

    fun onStudentPasswordEnrollStudentChanged(password: String?) = effect {
        _studentPasswordEnrollStudent.value = password
    }

    fun onStudentConfirmPasswordEnrollStudentChanged(confirm: String?) = effect {
        _studentConfirmPasswordEnrollStudent.value = confirm
    }

    fun onEnrollStudentExceptionChanged(exception: EnrollStudentException) = effect {
        _enrollStudentException.value = exception
    }

    fun onEnrollTeacherExceptionChanged(exception: EnrollTeacherException) = effect {
        _enrollTeacherException.value = exception
    }

    fun onCurrentPageMyAccountScreenChanged(page: Int?) = effect {
        Log.e(
            TAG,
            "onCurrentPageMyAccountScreenChanged: current page is $currentPageMyAccountScreen",
        )
        _currentPageMyAccountScreen.value = page
    }

    fun clearEnrollParentFields() = effect {
        _parentEmailEnrollParent.value = null
        _parentPasswordEnrollParent.value = null
        _parentConfirmPasswordEnrollParent.value = null
    }

    fun clearEnrollStudentFields() = effect {
        _studentEmailEnrollStudent.value = null
        _studentPasswordEnrollStudent.value = null
        _studentConfirmPasswordEnrollStudent.value = null
    }

    fun clearEnrollTeacherFields() = effect {
        _teacherEmailEnrollTeacher.value = null
        _teacherPasswordEnrollTeacher.value = null
        _teacherConfirmPasswordEnrollTeacher.value = null
    }

    fun saveUserAsVerified(user: UserNetworkModel, onResult: (Boolean) -> Unit) = effect {
        repository.saveUserAsVerified(user, onResult)
    }

    fun saveUsersAsVerified(users: List<UserNetworkModel>, onResult: (Boolean) -> Unit) = effect {
        repository.saveUsersAsVerified(users, onResult)
    }

    fun saveUserToStage(user: UserNetworkModel, onResult: (Boolean) -> Unit) = effect {
        repository.saveUserToStage(user, onResult)
    }

    fun saveUsersToStage(users: List<UserNetworkModel>, onResult: (Boolean) -> Unit) = effect {
        repository.saveUsersToStage(users, onResult)
    }

    fun getVerifiedUsersNetwork(
        schoolId: String,
    ) = effect {
        repository.getVerifiedUsersNetwork(schoolId) {
            _verifiedUsersNetwork.value = it
        }
    }

    fun getStagedUsersNetwork(
        schoolId: String,
    ) = effect {
        repository.getStagedUsersNetwork(schoolId) {
            _stagedUsersNetwork.value = it
        }
    }

    fun onTeacherAlreadyExistStateAdminChanged(state: Boolean?) = effect {
        _teacherAlreadyExistStateAdmin.value = state
    }

    fun onTeacherEmailEnrollTeacherChanged(email: String?) = effect {
        _teacherEmailEnrollTeacher.value = email
    }

    fun onTeacherPasswordEnrollTeacherChanged(password: String?) = effect {
        _teacherPasswordEnrollTeacher.value = password
    }

    fun onTeacherConfirmPasswordEnrollTeacherChanged(confirm: String?) = effect {
        _teacherConfirmPasswordEnrollTeacher.value = confirm
    }

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

    fun getUserByEmailNetwork(email: String) =
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

    override fun onCleared() {
        super.onCleared()
    }
}