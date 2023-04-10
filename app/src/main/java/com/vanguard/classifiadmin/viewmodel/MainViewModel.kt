package com.vanguard.classifiadmin.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseUser
import com.vanguard.classifiadmin.data.local.models.ClassModel
import com.vanguard.classifiadmin.data.local.models.FeedModel
import com.vanguard.classifiadmin.data.local.models.SchoolModel
import com.vanguard.classifiadmin.data.local.models.SubjectModel
import com.vanguard.classifiadmin.data.local.models.UserModel
import com.vanguard.classifiadmin.data.network.models.AssessmentNetworkModel
import com.vanguard.classifiadmin.data.network.models.ClassNetworkModel
import com.vanguard.classifiadmin.data.network.models.CommentNetworkModel
import com.vanguard.classifiadmin.data.network.models.FeedNetworkModel
import com.vanguard.classifiadmin.data.network.models.QuestionNetworkModel
import com.vanguard.classifiadmin.data.network.models.SchoolNetworkModel
import com.vanguard.classifiadmin.data.network.models.SubjectNetworkModel
import com.vanguard.classifiadmin.data.network.models.UserNetworkModel
import com.vanguard.classifiadmin.data.preferences.PrefDatastore
import com.vanguard.classifiadmin.data.repository.MainRepository
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.UserLoginState
import com.vanguard.classifiadmin.domain.workers.UploadFileToCacheWorker
import com.vanguard.classifiadmin.ui.components.AssessmentBottomSheetMode
import com.vanguard.classifiadmin.ui.screens.admin.EnrollParentException
import com.vanguard.classifiadmin.ui.screens.admin.EnrollStudentException
import com.vanguard.classifiadmin.ui.screens.admin.EnrollTeacherException
import com.vanguard.classifiadmin.ui.screens.admin.ManageClassAdminDetailFeature
import com.vanguard.classifiadmin.ui.screens.admin.ManageClassAdminDetailMessage
import com.vanguard.classifiadmin.ui.screens.admin.ManageClassMessage
import com.vanguard.classifiadmin.ui.screens.admin.ManageClassSubsectionItem
import com.vanguard.classifiadmin.ui.screens.admin.ManageSubjectAdminDetailFeature
import com.vanguard.classifiadmin.ui.screens.admin.ManageSubjectAdminDetailMessage
import com.vanguard.classifiadmin.ui.screens.admin.ManageSubjectMessage
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentCreationBottomSheetMode
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentCreationMessage
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentCreationOpenMode
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentState
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentType
import com.vanguard.classifiadmin.ui.screens.assessments.questions.CreateQuestionBottomSheetMode
import com.vanguard.classifiadmin.ui.screens.assessments.questions.CreateQuestionMessage
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionDifficulty
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionOption
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionOptionTrueFalse
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionType
import com.vanguard.classifiadmin.ui.screens.attempt.assessment.TakeAssessmentData
import com.vanguard.classifiadmin.ui.screens.classes.AcademicLevel
import com.vanguard.classifiadmin.ui.screens.classes.JoinClassOption
import com.vanguard.classifiadmin.ui.screens.dashboard.ClassFilterMode
import com.vanguard.classifiadmin.ui.screens.dashboard.DashboardBottomSheetFlavor
import com.vanguard.classifiadmin.ui.screens.dashboard.DashboardMessage
import com.vanguard.classifiadmin.ui.screens.feeds.FeedDetailMode
import com.vanguard.classifiadmin.ui.screens.importations.ImportTeacherRequest
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
    private val workManager: WorkManager,
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

    private var _selectedClassManageClass = MutableStateFlow(null as ClassModel?)
    val selectedClassManageClass: StateFlow<ClassModel?> = _selectedClassManageClass

    private var _currentDashboardBottomSheetFlavor =
        MutableStateFlow(null as DashboardBottomSheetFlavor?)
    val currentDashboardBottomSheetFlavor: StateFlow<DashboardBottomSheetFlavor?> =
        _currentDashboardBottomSheetFlavor


    private var _currentAssessmentOption = MutableStateFlow(null as AssessmentState?)
    val currentAssessmentOption: StateFlow<AssessmentState?> = _currentAssessmentOption

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

    private var _currentUserRolePref = MutableStateFlow(null as String?)
    val currentUserRolePref: StateFlow<String?> = _currentUserRolePref

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
    val selectedSubjectManageSubjectAdminBuffer: StateFlow<List<String>> =
        _selectedSubjectManageSubjectAdminBuffer

    private var _subjectSelectionListenerManageSubject = MutableStateFlow(0)
    val subjectSelectionListenerManageSubject: StateFlow<Int> =
        _subjectSelectionListenerManageSubject

    private var _selectedSubjectManageSubjectAdmin = MutableStateFlow(null as SubjectModel?)
    val selectedSubjectManageSubjectAdmin: StateFlow<SubjectModel?> =
        _selectedSubjectManageSubjectAdmin

    private var _manageSubjectMessage =
        MutableStateFlow(ManageSubjectMessage.NoMessage as ManageSubjectMessage)
    val manageSubjectMessage: StateFlow<ManageSubjectMessage> = _manageSubjectMessage

    private var _selectedClassManageSubjectAdminDetail = MutableStateFlow(null as ClassModel?)
    val selectedClassManageSubjectAdminDetail: StateFlow<ClassModel?> =
        _selectedClassManageSubjectAdminDetail

    private var _subjectNameManageSubjectAdminDetail = MutableStateFlow(null as String?)
    val subjectNameManageSubjectAdminDetail: StateFlow<String?> =
        _subjectNameManageSubjectAdminDetail

    private var _subjectCodeManageSubjectAdminDetail = MutableStateFlow(null as String?)
    val subjectCodeManageSubjectAdminDetail: StateFlow<String?> =
        _subjectCodeManageSubjectAdminDetail

    private var _manageSubjectAdminDetailMessage =
        MutableStateFlow(ManageSubjectAdminDetailMessage.NoMessage as ManageSubjectAdminDetailMessage)
    val manageSubjectAdminDetailMessage: StateFlow<ManageSubjectAdminDetailMessage> =
        _manageSubjectAdminDetailMessage

    private var _discussionTextCreateFeed = MutableStateFlow(null as String?)
    val discussionTextCreateFeed: StateFlow<String?> = _discussionTextCreateFeed

    private var _feedByIdNetwork =
        MutableStateFlow(Resource.Loading<FeedNetworkModel?>() as Resource<FeedNetworkModel?>)
    val feedByIdNetwork: StateFlow<Resource<FeedNetworkModel?>> = _feedByIdNetwork


    private var _stagedFeedsNetwork =
        MutableStateFlow(Resource.Loading<List<FeedNetworkModel>>() as Resource<List<FeedNetworkModel>>)
    val stagedFeedsNetwork: StateFlow<Resource<List<FeedNetworkModel>>> = _stagedFeedsNetwork

    private var _stagedFeedsByClassNetwork =
        MutableStateFlow(Resource.Loading<List<FeedNetworkModel>>() as Resource<List<FeedNetworkModel>>)
    val stagedFeedsByClassNetwork: StateFlow<Resource<List<FeedNetworkModel>>> =
        _stagedFeedsByClassNetwork

    private var _verifiedFeedsNetwork =
        MutableStateFlow(Resource.Loading<List<FeedNetworkModel>>() as Resource<List<FeedNetworkModel>>)
    val verifiedFeedsNetwork: StateFlow<Resource<List<FeedNetworkModel>>> = _verifiedFeedsNetwork

    private var _verifiedFeedsByClassNetwork =
        MutableStateFlow(Resource.Loading<List<FeedNetworkModel>>() as Resource<List<FeedNetworkModel>>)
    val verifiedFeedsByClassNetwork: StateFlow<Resource<List<FeedNetworkModel>>> =
        _verifiedFeedsByClassNetwork

    private var _classFilterBufferFeeds = MutableStateFlow(mutableListOf<String>())
    val classFilterBufferFeeds: StateFlow<List<String>> = _classFilterBufferFeeds

    private var _classFilterBufferReadFeeds = MutableStateFlow(mutableListOf<String>())
    val classFilterBufferReadFeeds: StateFlow<List<String>> = _classFilterBufferReadFeeds

    private var _classFilterBufferReadFeedsListener = MutableStateFlow(0)
    val classFilterBufferReadFeedsListener: StateFlow<Int> = _classFilterBufferReadFeedsListener

    private var _classFilterBufferFeedsListener = MutableStateFlow(0)
    val classFilterBufferFeedsListener: StateFlow<Int> = _classFilterBufferFeedsListener

    private var _verifiedClassesGivenTeacherNetwork =
        MutableStateFlow(Resource.Loading<List<ClassNetworkModel>>() as Resource<List<ClassNetworkModel>>)
    val verifiedClassesGivenTeacherNetwork: StateFlow<Resource<List<ClassNetworkModel>>> =
        _verifiedClassesGivenTeacherNetwork

    private var _verifiedClassesGivenStudentNetwork =
        MutableStateFlow(Resource.Loading<List<ClassNetworkModel>>() as Resource<List<ClassNetworkModel>>)
    val verifiedClassesGivenStudentNetwork: StateFlow<Resource<List<ClassNetworkModel>>> =
        _verifiedClassesGivenStudentNetwork

    private var _dashboardMessage = MutableStateFlow(DashboardMessage.NoMessage as DashboardMessage)
    val dashboardMessage: StateFlow<DashboardMessage> = _dashboardMessage

    private var _composeDiscussionState = MutableStateFlow(null as Boolean?)
    val composeDiscussionState: StateFlow<Boolean?> = _composeDiscussionState

    private var _feedActionListener = MutableStateFlow(0)
    val feedActionListener: StateFlow<Int> = _feedActionListener

    private var _feedDetailMode = MutableStateFlow(FeedDetailMode.Content as FeedDetailMode)
    val feedDetailMode: StateFlow<FeedDetailMode> = _feedDetailMode

    private var _selectedFeed = MutableStateFlow(null as FeedModel?)
    val selectedFeed: StateFlow<FeedModel?> = _selectedFeed

    private var _commentTextFieldState = MutableStateFlow(null as Boolean?)
    val commentTextFieldState: StateFlow<Boolean?> = _commentTextFieldState

    private var _commentTextFeed = MutableStateFlow(null as String?)
    val commentTextFeed: StateFlow<String?> = _commentTextFeed

    private var _commentByIdNetwork =
        MutableStateFlow(Resource.Loading<CommentNetworkModel?>() as Resource<CommentNetworkModel?>)
    val commentByIdNetwork: StateFlow<Resource<CommentNetworkModel?>> = _commentByIdNetwork

    private var _commentsByFeedNetwork =
        MutableStateFlow(Resource.Loading<List<CommentNetworkModel>>() as Resource<List<CommentNetworkModel>>)
    val commentsByFeedNetwork: StateFlow<Resource<List<CommentNetworkModel>>> =
        _commentsByFeedNetwork

    private var _classFilterMode = MutableStateFlow(ClassFilterMode.ReadFeeds as ClassFilterMode)
    val classFilterMode: StateFlow<ClassFilterMode> = _classFilterMode

    private var _currentClassFeedPref = MutableStateFlow(null as String?)
    val currentClassFeedPref: StateFlow<String?> = _currentClassFeedPref

    private var _currentAssessmentType = MutableStateFlow(AssessmentType.HomeWork as AssessmentType)
    val currentAssessmentType: StateFlow<AssessmentType> = _currentAssessmentType

    private var _assessmentNameCreateAssessment = MutableStateFlow(null as String?)
    val assessmentNameCreateAssessment: StateFlow<String?> = _assessmentNameCreateAssessment

    private var _verifiedTeachersUnderSubjectNetwork =
        MutableStateFlow(Resource.Loading<List<UserNetworkModel>>() as Resource<List<UserNetworkModel>>)
    val verifiedTeachersUnderSubjectNetwork: StateFlow<Resource<List<UserNetworkModel>>> =
        _verifiedTeachersUnderSubjectNetwork

    private var _manageSubjectAdminDetailBuffer = MutableStateFlow(mutableListOf<String>())
    var manageSubjectAdminDetailBuffer: StateFlow<List<String>> = _manageSubjectAdminDetailBuffer

    private var _manageSubjectAdminDetailBufferListener = MutableStateFlow(0)
    var manageSubjectAdminDetailBufferListener: StateFlow<Int> =
        _manageSubjectAdminDetailBufferListener

    private var _importTeacherRequest = MutableStateFlow(null as ImportTeacherRequest?)
    val importTeacherRequest: StateFlow<ImportTeacherRequest?> = _importTeacherRequest

    private var _manageSubjectAdminDetailFeature =
        MutableStateFlow(null as ManageSubjectAdminDetailFeature?)
    val manageSubjectAdminDetailFeature: StateFlow<ManageSubjectAdminDetailFeature?> =
        _manageSubjectAdminDetailFeature

    private var _assessmentBottomSheetMode =
        MutableStateFlow(AssessmentBottomSheetMode.Students as AssessmentBottomSheetMode)
    val assessmentBottomSheetMode: StateFlow<AssessmentBottomSheetMode> = _assessmentBottomSheetMode

    private var _verifiedSubjectsGivenTeacherNetwork =
        MutableStateFlow(Resource.Loading<List<SubjectNetworkModel>>() as Resource<List<SubjectNetworkModel>>)
    val verifiedSubjectsGivenTeacherNetwork: StateFlow<Resource<List<SubjectNetworkModel>>> =
        _verifiedSubjectsGivenTeacherNetwork

    private var _selectedSubjectCreateAssessment = MutableStateFlow(null as SubjectModel?)
    val selectedSubjectCreateAssessment: StateFlow<SubjectModel?> = _selectedSubjectCreateAssessment

    private var _studentsBufferCreateAssessment = MutableStateFlow(mutableListOf<UserModel>())
    val studentsBufferCreateAssessment: StateFlow<List<UserModel>> = _studentsBufferCreateAssessment

    private var _studentsBufferCreateAssessmentListener = MutableStateFlow(0)
    val studentsBufferCreateAssessmentListener: StateFlow<Int> =
        _studentsBufferCreateAssessmentListener

    private var _startTimeCreateAssessment = MutableStateFlow(null as String?)
    val startTimeCreateAssessment: StateFlow<String?> = _startTimeCreateAssessment

    private var _endTimeCreateAssessment = MutableStateFlow(null as String?)
    val endTimeCreateAssessment: StateFlow<String?> = _endTimeCreateAssessment

    private var _startDateCreateAssessment = MutableStateFlow(null as String?)
    val startDateCreateAssessment: StateFlow<String?> = _startDateCreateAssessment

    private var _endDateCreateAssessment = MutableStateFlow(null as String?)
    val endDateCreateAssessment: StateFlow<String?> = _endDateCreateAssessment

    private var _assessmentByIdNetwork =
        MutableStateFlow(Resource.Loading<AssessmentNetworkModel?>() as Resource<AssessmentNetworkModel?>)
    val assessmentByIdNetwork: StateFlow<Resource<AssessmentNetworkModel?>> = _assessmentByIdNetwork

    private var _verifiedAssessmentsNetwork =
        MutableStateFlow(Resource.Loading<List<AssessmentNetworkModel>>() as Resource<List<AssessmentNetworkModel>>)
    val verifiedAssessmentsNetwork: StateFlow<Resource<List<AssessmentNetworkModel>>> =
        _verifiedAssessmentsNetwork

    private var _stagedAssessmentsNetwork =
        MutableStateFlow(Resource.Loading<List<AssessmentNetworkModel>>() as Resource<List<AssessmentNetworkModel>>)
    val stagedAssessmentsNetwork: StateFlow<Resource<List<AssessmentNetworkModel>>> =
        _stagedAssessmentsNetwork

    private var _assessmentCreationBottomSheetMode =
        MutableStateFlow(AssessmentCreationBottomSheetMode.AddQuestion as AssessmentCreationBottomSheetMode)
    val assessmentCreationBottomSheetMode: StateFlow<AssessmentCreationBottomSheetMode> =
        _assessmentCreationBottomSheetMode

    private var _questionSidePanelState = MutableStateFlow(null as Boolean?)
    val questionSidePanelState: StateFlow<Boolean?> = _questionSidePanelState

    private var _createQuestionBottomSheetMode =
        MutableStateFlow(CreateQuestionBottomSheetMode.QuestionType as CreateQuestionBottomSheetMode)
    val createQuestionBottomSheetMode: StateFlow<CreateQuestionBottomSheetMode> =
        _createQuestionBottomSheetMode

    private var _correctQuestionOption = MutableStateFlow(QuestionOption.OptionA as QuestionOption)
    val correctQuestionOption: StateFlow<QuestionOption> = _correctQuestionOption

    private var _questionBodyCreateQuestion = MutableStateFlow(null as String?)
    var questionBodyCreateQuestion: StateFlow<String?> = _questionBodyCreateQuestion

    private var _questionOptionACreateQuestion = MutableStateFlow(null as String?)
    var questionOptionACreateQuestion: StateFlow<String?> = _questionOptionACreateQuestion

    private var _questionOptionBCreateQuestion = MutableStateFlow(null as String?)
    var questionOptionBCreateQuestion: StateFlow<String?> = _questionOptionBCreateQuestion

    private var _questionOptionCCreateQuestion = MutableStateFlow(null as String?)
    var questionOptionCCreateQuestion: StateFlow<String?> = _questionOptionCCreateQuestion

    private var _questionOptionDCreateQuestion = MutableStateFlow(null as String?)
    var questionOptionDCreateQuestion: StateFlow<String?> = _questionOptionDCreateQuestion


    private var _questionDifficultyCreateQuestion =
        MutableStateFlow(QuestionDifficulty.Easy as QuestionDifficulty)
    val questionDifficultyCreateQuestion: StateFlow<QuestionDifficulty> =
        _questionDifficultyCreateQuestion

    private var _questionTypeCreateQuestion =
        MutableStateFlow(QuestionType.MultiChoice as QuestionType)
    val questionTypeCreateQuestion: StateFlow<QuestionType> = _questionTypeCreateQuestion

    private var _questionScoreCreateQuestion = MutableStateFlow(null as String?)
    var questionScoreCreateQuestion: StateFlow<String?> = _questionScoreCreateQuestion

    private var _assessmentDurationCreateQuestion = MutableStateFlow(null as String?)
    var assessmentDurationCreateQuestion: StateFlow<String?> = _assessmentDurationCreateQuestion

    private var _questionAnswersCreateQuestion = MutableStateFlow(mutableListOf<String>())
    var questionAnswersCreateQuestion: StateFlow<List<String>> = _questionAnswersCreateQuestion

    private var _correctAnswerTrueFalse = MutableStateFlow(QuestionOptionTrueFalse.False)
    val correctAnswerTrueFalse: StateFlow<QuestionOptionTrueFalse> = _correctAnswerTrueFalse

    private var _correctShortAnswerCreateQuestion = MutableStateFlow(null as String?)
    var correctShortAnswerCreateQuestion: StateFlow<String?> = _correctShortAnswerCreateQuestion

    private var _stagedQuestionsNetwork =
        MutableStateFlow(Resource.Loading<List<QuestionNetworkModel>>() as Resource<List<QuestionNetworkModel>>)
    val stagedQuestionsNetwork: StateFlow<Resource<List<QuestionNetworkModel>>> =
        _stagedQuestionsNetwork

    private var _verifiedQuestionsNetwork =
        MutableStateFlow(Resource.Loading<List<QuestionNetworkModel>>() as Resource<List<QuestionNetworkModel>>)
    val verifiedQuestionsNetwork: StateFlow<Resource<List<QuestionNetworkModel>>> =
        _verifiedQuestionsNetwork

    private var _questionByIdNetwork =
        MutableStateFlow(Resource.Loading<QuestionNetworkModel?>() as Resource<QuestionNetworkModel?>)
    val questionByIdNetwork: StateFlow<Resource<QuestionNetworkModel?>> =
        _questionByIdNetwork

    private var _createQuestionMessage =
        MutableStateFlow(CreateQuestionMessage.NoMessage as CreateQuestionMessage)
    val createQuestionMessage: StateFlow<CreateQuestionMessage> = _createQuestionMessage

    private var _assessmentCreationMessage =
        MutableStateFlow(AssessmentCreationMessage.NoMessage as AssessmentCreationMessage)
    val assessmentCreationMessage: StateFlow<AssessmentCreationMessage> = _assessmentCreationMessage

    private var _verifiedAssessmentsDraftNetwork =
        MutableStateFlow(Resource.Loading<List<AssessmentNetworkModel>>() as Resource<List<AssessmentNetworkModel>>)
    val verifiedAssessmentsDraftNetwork: StateFlow<Resource<List<AssessmentNetworkModel>>> =
        _verifiedAssessmentsDraftNetwork

    private var _verifiedAssessmentsDraftForClassNetwork =
        MutableStateFlow(Resource.Loading<List<AssessmentNetworkModel>>() as Resource<List<AssessmentNetworkModel>>)
    val verifiedAssessmentsDraftForClassNetwork: StateFlow<Resource<List<AssessmentNetworkModel>>> =
        _verifiedAssessmentsDraftForClassNetwork

    private var _verifiedAssessmentsInReviewNetwork =
        MutableStateFlow(Resource.Loading<List<AssessmentNetworkModel>>() as Resource<List<AssessmentNetworkModel>>)
    val verifiedAssessmentsInReviewNetwork: StateFlow<Resource<List<AssessmentNetworkModel>>> =
        _verifiedAssessmentsInReviewNetwork

    private var _verifiedAssessmentsInReviewForClassNetwork =
        MutableStateFlow(Resource.Loading<List<AssessmentNetworkModel>>() as Resource<List<AssessmentNetworkModel>>)
    val verifiedAssessmentsInReviewForClassNetwork: StateFlow<Resource<List<AssessmentNetworkModel>>> =
        _verifiedAssessmentsInReviewForClassNetwork

    private var _verifiedAssessmentsPublishedNetwork =
        MutableStateFlow(Resource.Loading<List<AssessmentNetworkModel>>() as Resource<List<AssessmentNetworkModel>>)
    val verifiedAssessmentsPublishedNetwork: StateFlow<Resource<List<AssessmentNetworkModel>>> =
        _verifiedAssessmentsPublishedNetwork

    private var _verifiedAssessmentsPublishedForClassNetwork =
        MutableStateFlow(Resource.Loading<List<AssessmentNetworkModel>>() as Resource<List<AssessmentNetworkModel>>)
    val verifiedAssessmentsPublishedForClassNetwork: StateFlow<Resource<List<AssessmentNetworkModel>>> =
        _verifiedAssessmentsPublishedForClassNetwork

    private var _assessmentCreationOpenMode =
        MutableStateFlow(AssessmentCreationOpenMode.Editor as AssessmentCreationOpenMode)
    val assessmentCreationOpenMode: StateFlow<AssessmentCreationOpenMode> =
        _assessmentCreationOpenMode

    private var _currentAssessmentIdDraft = MutableStateFlow(null as String?)
    val currentAssessmentIdDraft: StateFlow<String?> = _currentAssessmentIdDraft

    private var _currentFeedIdPending = MutableStateFlow(null as String?)
    val currentFeedIdPending: StateFlow<String?> = _currentFeedIdPending

    private var _verifiedQuestionsByAssessmentNetwork =
        MutableStateFlow(Resource.Loading<List<QuestionNetworkModel>>() as Resource<List<QuestionNetworkModel>>)
    val verifiedQuestionsByAssessmentNetwork: StateFlow<Resource<List<QuestionNetworkModel>>> =
        _verifiedQuestionsByAssessmentNetwork

    private var _selectedQuestionIdCreateQuestion = MutableStateFlow(null as String?)
    val selectedQuestionIdCreateQuestion: StateFlow<String?> = _selectedQuestionIdCreateQuestion

    private var _currentAssessmentIdPublished = MutableStateFlow(null as String?)
    val currentAssessmentIdPublished: StateFlow<String?> = _currentAssessmentIdPublished

    private var _isAssessmentComplete = MutableStateFlow(false)
    val isAssessmentComplete: StateFlow<Boolean> = _isAssessmentComplete

    private var _isNextQuestionEnabled = MutableStateFlow(false)
    val isNextQuestionEnabled: StateFlow<Boolean> = _isNextQuestionEnabled

    private var _takeAssessmentData = MutableStateFlow(null as TakeAssessmentData?)
    val takeAssessmentData: StateFlow<TakeAssessmentData?> = _takeAssessmentData

    fun onNextQuestionEnabledChanged(enabled: Boolean) = effect {
        _isNextQuestionEnabled.value = enabled
    }
    fun onNextPressed() = effect {

    }

    fun onPreviousPressed() = effect {

    }

    fun onDonePressed() = effect {

    }
    fun onTakeAssessmentDataChanged(data: TakeAssessmentData?) = effect {
        _takeAssessmentData.value = data
    }

    fun onAssessmentCompleteChanged(complete: Boolean) = effect {
        _isAssessmentComplete.value = complete
    }

    fun onCurrentAssessmentIdPublishedChanged(assessmentId: String?) = effect {
        _currentAssessmentIdPublished.value = assessmentId
    }

    fun onCurrentFeedIdPendingChanged(feedId: String?) = effect {
        _currentFeedIdPending.value = feedId
    }

    fun onSelectedQuestionIdCreateQuestionChanged(questionId: String?) = effect {
        _selectedQuestionIdCreateQuestion.value = questionId
    }

    fun getVerifiedQuestionsByAssessmentNetwork(
        assessmentId: String,
        schoolId: String,
    ) = effect {
        repository.getVerifiedQuestionsByAssessmentNetwork(
            assessmentId, schoolId
        ) {
            _verifiedQuestionsByAssessmentNetwork.value = it
        }
    }


    fun onCurrentAssessmentIdDraftChanged(assessmentId: String?) = effect {
        _currentAssessmentIdDraft.value = assessmentId
    }


    fun onAssessmentCreationOpenModeChanged(mode: AssessmentCreationOpenMode) = effect {
        _assessmentCreationOpenMode.value = mode
    }


    fun getVerifiedAssessmentsDraftForClassNetwork(
        classId: String,
        schoolId: String,
    ) = effect {
        repository.getVerifiedAssessmentsDraftForClassNetwork(classId, schoolId) {
            _verifiedAssessmentsDraftForClassNetwork.value = it
        }
    }

    fun getVerifiedAssessmentsInReviewForClassNetwork(
        classId: String,
        schoolId: String,
    ) = effect {
        repository.getVerifiedAssessmentsInReviewForClassNetwork(classId, schoolId) {
            _verifiedAssessmentsInReviewForClassNetwork.value = it
        }
    }

    fun getVerifiedAssessmentsPublishedForClassNetwork(
        classId: String,
        schoolId: String,
    ) = effect {
        repository.getVerifiedAssessmentsPublishedForClassNetwork(classId, schoolId) {
            _verifiedAssessmentsPublishedForClassNetwork.value = it
        }
    }

    fun getVerifiedAssessmentsDraftNetwork(
        schoolId: String,
    ) = effect {
        repository.getVerifiedAssessmentsDraftNetwork(schoolId) {
            _verifiedAssessmentsDraftNetwork.value = it
        }
    }

    fun getVerifiedAssessmentsInReviewNetwork(
        schoolId: String,
    ) = effect {
        repository.getVerifiedAssessmentsInReviewNetwork(schoolId) {
            _verifiedAssessmentsInReviewNetwork.value = it
        }
    }

    fun getVerifiedAssessmentsPublishedNetwork(
        schoolId: String,
    ) = effect {
        repository.getVerifiedAssessmentsPublishedNetwork(schoolId) {
            _verifiedAssessmentsPublishedNetwork.value = it
        }
    }

    fun onAssessmentCreationMessageChanged(message: AssessmentCreationMessage) = effect {
        _assessmentCreationMessage.value = message
    }

    fun clearCreateQuestionFields() = effect {
        _questionBodyCreateQuestion.value = null
        _questionOptionACreateQuestion.value = null
        _questionOptionBCreateQuestion.value = null
        _questionOptionCCreateQuestion.value = null
        _questionOptionDCreateQuestion.value = null
        _questionAnswersCreateQuestion.value.clear()
        _correctShortAnswerCreateQuestion.value = null
        _questionScoreCreateQuestion.value = null
        _questionDifficultyCreateQuestion.value = QuestionDifficulty.Easy
        _questionTypeCreateQuestion.value = QuestionType.MultiChoice
        Log.e(TAG, "clearCreateQuestionFields: clearing the create question fields")
    }

    fun deleteStagedQuestionsByUserNetwork(
        authorId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository
            .deleteStagedQuestionsByUserNetwork(authorId, schoolId, onResult)
    }

    fun deleteStagedAssessmentsByUserNetwork(
        authorId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.deleteStagedAssessmentsByUserNetwork(
            authorId, schoolId, onResult
        )
    }


    fun onCreateQuestionMessageChanged(message: CreateQuestionMessage) = effect {
        _createQuestionMessage.value = message
    }

    //question
    fun saveQuestionAsStagedNetwork(
        question: QuestionNetworkModel,
        onResult: (Boolean) -> Unit,
    ) = effect {
        repository.saveQuestionAsStagedNetwork(question, onResult)
    }

    fun saveQuestionAsVerifiedNetwork(
        question: QuestionNetworkModel,
        onResult: (Boolean) -> Unit,
    ) = effect {
        repository.saveQuestionAsVerifiedNetwork(question, onResult)
    }

    fun deleteQuestionNetwork(
        question: QuestionNetworkModel,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.deleteQuestionNetwork(question, onResult)
    }

    fun deleteQuestionByIdNetwork(
        questionId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.deleteQuestionByIdNetwork(questionId, schoolId, onResult)
    }

    fun getQuestionByIdNetwork(
        questionId: String,
        schoolId: String,
    ) = effect {
        repository.getQuestionByIdNetwork(questionId, schoolId) {
            _questionByIdNetwork.value = it
        }
    }

    fun getVerifiedQuestionsNetwork(
        schoolId: String,
    ) = effect {
        repository.getVerifiedQuestionsNetwork(schoolId) {
            _verifiedQuestionsNetwork.value = it
        }
    }

    fun getStagedQuestionsNetwork(
        schoolId: String,
        authorId: String,
    ) = effect {
        repository.getStagedQuestionsNetwork(schoolId, authorId) {
            _stagedQuestionsNetwork.value = it
        }
    }


    fun onQuestionShortAnswerCreateQuestionChanged(answer: String?) = effect {
        _correctShortAnswerCreateQuestion.value = answer
    }

    fun onCorrectAnswerTrueFalseChanged(answer: QuestionOptionTrueFalse) = effect {
        _correctAnswerTrueFalse.value = answer
    }

    fun onAddToAnswersCreateQuestion(answer: String) = effect {
        if (!_questionAnswersCreateQuestion.value.contains(answer)) {
            _questionAnswersCreateQuestion.value.add(answer)
        }
    }

    fun onRemoveAnswersCreateQuestion(answer: String) = effect {
        if (_questionAnswersCreateQuestion.value.contains(answer)) {
            _questionAnswersCreateQuestion.value.remove(answer)
        }
    }

    fun clearAnswersCreateQuestion() = effect {
        _questionAnswersCreateQuestion.value.clear()
    }

    fun onAssessmentDurationCreateQuestionChanged(duration: String?) = effect {
        _assessmentDurationCreateQuestion.value = duration
    }

    fun onQuestionScoreCreateQuestionChanged(score: String?) = effect {
        _questionScoreCreateQuestion.value = score
    }

    fun onQuestionDifficultyChanged(difficulty: QuestionDifficulty) = effect {
        _questionDifficultyCreateQuestion.value = difficulty
    }

    fun onQuestionTypeChanged(type: QuestionType) = effect {
        _questionTypeCreateQuestion.value = type
    }

    fun onQuestionBodyCreateQuestionChanged(body: String?) = effect {
        _questionBodyCreateQuestion.value = body
    }

    fun onQuestionOptionACreateQuestionChanged(optionA: String?) = effect {
        _questionOptionACreateQuestion.value = optionA
    }

    fun onQuestionOptionBCreateQuestionChanged(optionC: String?) = effect {
        _questionOptionBCreateQuestion.value = optionC
    }

    fun onQuestionOptionCCreateQuestionChanged(optionC: String?) = effect {
        _questionOptionCCreateQuestion.value = optionC
    }

    fun onQuestionOptionDCreateQuestionChanged(optionD: String?) = effect {
        _questionOptionDCreateQuestion.value = optionD
    }

    fun onCorrectQuestionOptionChanged(option: QuestionOption) = effect {
        _correctQuestionOption.value = option
    }

    fun onCreateQuestionBottomSheetModeChanged(mode: CreateQuestionBottomSheetMode) = effect {
        _createQuestionBottomSheetMode.value = mode
    }

    fun onQuestionSidePanelStateChanged(state: Boolean?) = effect {
        _questionSidePanelState.value = state
    }

    fun onAssessmentCreationBottomSheetModeChanged(mode: AssessmentCreationBottomSheetMode) =
        effect {
            _assessmentCreationBottomSheetMode.value = mode
        }

    //assessment
    fun saveAssessmentAsStagedNetwork(
        assessment: AssessmentNetworkModel,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.saveAssessmentAsStagedNetwork(assessment, onResult)
    }

    fun saveAssessmentAsVerifiedNetwork(
        assessment: AssessmentNetworkModel,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.saveAssessmentAsVerifiedNetwork(assessment, onResult)
    }

    fun deleteAssessmentNetwork(
        assessment: AssessmentNetworkModel,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.deleteAssessmentNetwork(assessment, onResult)
    }

    fun deleteAssessmentByIdNetwork(
        assessmentId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.deleteAssessmentByIdNetwork(assessmentId, schoolId, onResult)
    }

    fun getStagedAssessmentsNetwork(
        authorId: String,
        schoolId: String,
    ) = effect {
        repository.getStagedAssessmentsNetwork(authorId, schoolId) {
            _stagedAssessmentsNetwork.value = it
        }
    }

    fun getVerifiedAssessmentsNetwork(
        schoolId: String,
    ) = effect {
        repository.getVerifiedAssessmentsNetwork(schoolId) {
            _verifiedAssessmentsNetwork.value = it
        }
    }

    fun getAssessmentByIdNetwork(
        assessmentId: String,
        schoolId: String,
    ) = effect {
        repository.getAssessmentByIdNetwork(assessmentId, schoolId) {
            _assessmentByIdNetwork.value = it
        }

    }

    fun onStartTimeCreateAssessmentChanged(time: String?) = effect {
        _startTimeCreateAssessment.value = time
    }

    fun onEndTimeCreateAssessmentChanged(time: String?) = effect {
        _endTimeCreateAssessment.value = time
    }

    fun onStartDateCreateAssessmentChanged(date: String?) = effect {
        _startDateCreateAssessment.value = date
    }

    fun onEndDateCreateAssessmentChanged(date: String?) = effect {
        _endDateCreateAssessment.value = date
    }

    fun onIncStudentsBufferCreateAssessmentListener() = effect {
        _studentsBufferCreateAssessmentListener.value++
    }

    fun onDecStudentsBufferCreateAssessmentListener() = effect {
        _studentsBufferCreateAssessmentListener.value--
    }

    fun onAddStudentToAssessment(student: UserModel) = effect {
        if (!_studentsBufferCreateAssessment.value.contains(student)) {
            _studentsBufferCreateAssessment.value.add(student)
        }
    }

    fun onRemoveStudentFromAssessment(student: UserModel) = effect {
        if (_studentsBufferCreateAssessment.value.contains(student)) {
            _studentsBufferCreateAssessment.value.remove(student)
        }
    }

    fun clearStudentBufferCreateAssessment() = effect {
        _studentsBufferCreateAssessment.value.clear()
    }

    fun onSelectedSubjectCreateAssessmentChanged(subject: SubjectModel?) = effect {
        _selectedSubjectCreateAssessment.value = subject
    }

    fun getVerifiedSubjectsGivenTeacherNetwork(
        teacherId: String,
        schoolId: String,
    ) = effect {
        repository.getVerifiedSubjectsGivenTeacherNetwork(teacherId, schoolId) {
            _verifiedSubjectsGivenTeacherNetwork.value = it
        }
    }


    fun onAssessmentBottomSheetModeChanged(mode: AssessmentBottomSheetMode) = effect {
        _assessmentBottomSheetMode.value = mode
    }

    fun onManageSubjectAdminDetailFeatureChanged(feature: ManageSubjectAdminDetailFeature?) =
        effect {
            _manageSubjectAdminDetailFeature.value = feature
        }

    fun onImportTeacherRequestChanged(request: ImportTeacherRequest?) = effect {
        _importTeacherRequest.value = request
    }

    fun onIncManageSubjectAdminDetailBufferListener() = effect {
        _manageSubjectAdminDetailBufferListener.value++
    }

    fun onDecManageSubjectAdminDetailBufferListener() = effect {
        _manageSubjectAdminDetailBufferListener.value--
    }

    fun onAddToSubjectBufferManageSubjectAdminDetail(teacherId: String) = effect {
        if (!_manageSubjectAdminDetailBuffer.value.contains(teacherId)) {
            _manageSubjectAdminDetailBuffer.value.add(teacherId)
        }
    }

    fun onRemoveFromSubjectBufferManageSubjectAdminDetail(teacherId: String) = effect {
        if (_manageSubjectAdminDetailBuffer.value.contains(teacherId)) {
            _manageSubjectAdminDetailBuffer.value.remove(teacherId)
        }
    }

    fun clearSubjectBufferManageSubjectAdminDetail() = effect {
        _manageSubjectAdminDetailBuffer.value.clear()
    }

    fun getVerifiedTeachersUnderSubjectNetwork(
        subject: SubjectNetworkModel
    ) = effect {
        repository.getVerifiedTeachersUnderSubjectNetwork(subject) {
            _verifiedTeachersUnderSubjectNetwork.value = it
        }
    }


    fun onAssessmentNameCreateAssessmentChanged(name: String?) = effect {
        _assessmentNameCreateAssessment.value = name
    }

    fun onCurrentAssessmentTypeChanged(type: AssessmentType) = effect {
        _currentAssessmentType.value = type
    }

    fun deleteStagedFeedsNetwork(
        schoolId: String,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.deleteStagedFeedsNetwork(schoolId, onResult)
    }

    fun saveCurrentClassFeedPref(classId: String, onResult: (Boolean) -> Unit) = effect {
        store.saveCurrentClassFeedPref(classId, onResult)
    }

    fun onAddToClassFilterBufferReadFeeds(classId: String) = effect {
        if (!_classFilterBufferReadFeeds.value.contains(classId)) {
            _classFilterBufferReadFeeds.value.clear()
            _classFilterBufferReadFeeds.value.add(classId)
        }
    }

    fun onRemoveFromClassFilterBufferReadFeeds(classId: String) = effect {
        if (_classFilterBufferReadFeeds.value.contains(classId)) {
            _classFilterBufferReadFeeds.value.remove(classId)
        }
    }

    fun clearClassFilterBufferReadFeeds() = effect {
        _classFilterBufferReadFeeds.value.clear()
    }

    fun onIncClassFilterBufferReadFeedsListener() = effect {
        _classFilterBufferReadFeedsListener.value++
    }

    fun onDecClassFilterBufferReadFeedsListener() = effect {
        _classFilterBufferReadFeedsListener.value--
    }

    fun getCurrentClassFeedPref() = effect {
        store.currentClassFeedPref.collect { currentClass ->
            _currentClassFeedPref.value = currentClass
        }
    }

    fun onClassFilterModeChanged(mode: ClassFilterMode) = effect {
        _classFilterMode.value = mode
    }

    fun uploadFileToCache(uri: Uri) = effect {
        val uploadFileRequest = OneTimeWorkRequestBuilder<UploadFileToCacheWorker>()
            .setInputData(UploadFileToCacheWorker.collectUri(uri.toString()))
            .build()
        workManager.enqueue(uploadFileRequest)
    }

    //comments
    fun saveCommentNetwork(
        comment: CommentNetworkModel,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.saveCommentNetwork(comment, onResult)
    }

    fun getCommentByIdNetwork(
        commentId: String,
        feedId: String,
        schoolId: String,
    ) = effect {
        repository.getCommentByIdNetwork(
            commentId, feedId, schoolId
        ) {
            _commentByIdNetwork.value = it
        }
    }

    fun getCommentsByFeedNetwork(
        feedId: String,
        schoolId: String,
    ) = effect {
        repository.getCommentsByFeedNetwork(feedId, schoolId) {
            _commentsByFeedNetwork.value = it
        }
    }

    fun deleteCommentNetwork(
        comment: CommentNetworkModel,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.deleteCommentNetwork(comment, onResult)
    }

    fun deleteCommentByIdNetwork(
        commentId: String,
        feedId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.deleteCommentByIdNetwork(
            commentId, feedId, schoolId, onResult
        )
    }

    fun onCommentTextFeedChanged(comment: String?) = effect {
        _commentTextFeed.value = comment
    }

    fun clearCommentTextField() = effect {
        _commentTextFeed.value = null
    }

    fun onCommentTextFieldStateChanged(state: Boolean?) = effect {
        _commentTextFieldState.value = state
    }

    fun onSelectedFeedChanged(feed: FeedModel?) = effect { _selectedFeed.value = feed }

    fun onFeedDetailModeChanged(mode: FeedDetailMode) = effect { _feedDetailMode.value = mode }

    fun onIncFeedActionListener() = effect {
        _feedActionListener.value++
    }

    fun onDecFeedActionListener() = effect {
        _feedActionListener.value--
    }

    fun clearDiscussionTextField() = effect {
        _discussionTextCreateFeed.value = null
    }

    fun onComposeDiscussionStateChanged(state: Boolean?) = effect {
        _composeDiscussionState.value = state
    }

    fun onDashboardMessageChanged(message: DashboardMessage) = effect {
        _dashboardMessage.value = message
    }

    fun getVerifiedClassesGivenTeacherNetwork(
        teacherId: String,
        schoolId: String,
    ) = effect {
        repository.getVerifiedClassesGivenTeacherNetwork(teacherId, schoolId) {
            _verifiedClassesGivenTeacherNetwork.value = it
        }
    }

    fun getVerifiedClassesGivenStudentNetwork(
        studentId: String,
        schoolId: String,
    ) = effect {
        repository.getVerifiedClassesGivenStudentNetwork(studentId, schoolId) {
            _verifiedClassesGivenStudentNetwork.value = it
        }
    }

    fun onIncClassFilterBufferFeedsListener() = effect {
        _classFilterBufferFeedsListener.value++
    }

    fun onDecClassFilterBufferFeedsListener() = effect {
        _classFilterBufferFeedsListener.value--
    }

    fun onAddToClassFilterBufferFeeds(classId: String) = effect {
        if (!_classFilterBufferFeeds.value.contains(classId)) {
            _classFilterBufferFeeds.value.add(classId)
        }
    }

    fun onRemoveFromClassFilterBufferFeeds(classId: String) = effect {
        if (_classFilterBufferFeeds.value.contains(classId)) {
            _classFilterBufferFeeds.value.remove(classId)
        }
    }

    fun clearClassFilterBufferFeeds() = effect {
        _classFilterBufferFeeds.value.clear()
    }

    // Feed
    fun saveFeedAsStagedNetwork(
        feed: FeedNetworkModel,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.saveFeedAsStagedNetwork(
            feed, onResult
        )
    }

    fun saveFeedAsVerifiedNetwork(
        feed: FeedNetworkModel,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.saveFeedAsVerifiedNetwork(
            feed, onResult
        )
    }

    fun getFeedByIdNetwork(
        feedId: String,
        schoolId: String,
    ) = effect {
        repository.getFeedByIdNetwork(feedId, schoolId) {
            _feedByIdNetwork.value = it
        }
    }

    fun getStagedFeedsNetwork(
        schoolId: String,
    ) = effect {
        repository.getStagedFeedsNetwork(schoolId) {
            _stagedFeedsNetwork.value = it
        }
    }

    fun getStagedFeedsByClassNetwork(
        classId: String,
        schoolId: String,
    ) = effect {
        repository.getStagedFeedsByClassNetwork(classId, schoolId) {
            _stagedFeedsByClassNetwork.value = it
        }
    }

    fun getVerifiedFeedsNetwork(
        schoolId: String,
    ) = effect {
        repository.getVerifiedFeedsNetwork(schoolId) {
            _verifiedFeedsNetwork.value = it
        }
    }

    fun getVerifiedFeedsByClassNetwork(
        classId: String,
        schoolId: String,
    ) = effect {
        repository.getVerifiedFeedsByClassNetwork(classId, schoolId) {
            _verifiedFeedsByClassNetwork.value = it
        }
    }

    fun deleteFeedNetwork(
        feed: FeedNetworkModel,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.deleteFeedNetwork(
            feed, onResult
        )
    }

    fun deleteFeedByIdNetwork(
        feedId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    ) = effect {
        repository.deleteFeedByIdNetwork(
            feedId, schoolId, onResult
        )
    }


    fun onDiscussionTextCreateFeedChanged(text: String?) = effect {
        _discussionTextCreateFeed.value = text
    }

    fun onManageSubjectAdminDetailMessageChanged(message: ManageSubjectAdminDetailMessage) =
        effect {
            _manageSubjectAdminDetailMessage.value = message
        }

    fun onSubjectNameManageSubjectAdminDetailChanged(name: String?) = effect {
        _subjectNameManageSubjectAdminDetail.value = name
    }

    fun onSubjectCodeManageSubjectAdminDetailChanged(code: String?) = effect {
        _subjectCodeManageSubjectAdminDetail.value = code
    }

    fun onSelectedClassManageSubjectAdminDetailChanged(myClass: ClassModel?) = effect {
        _selectedClassManageSubjectAdminDetail.value = myClass
    }

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

    fun getCurrentUserRolePref() = effect {
        store.currentUserRole.collect { role ->
            _currentUserRolePref.value = role
        }
    }

    fun saveCurrentUserRolePref(role: String, onResult: (Boolean) -> Unit) = effect {
        store.saveCurrentUserRole(role, onResult)
    }

    fun saveCurrentProfileImagePref(downloadUrl: String, onResult: (Boolean) -> Unit) = effect {
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

    fun onCurrentAssessmentOptionChanged(option: AssessmentState?) = effect {
        _currentAssessmentOption.value = option
    }

    fun onCurrentDashboardBottomSheetFlavorChanged(flavor: DashboardBottomSheetFlavor?) = effect {
        _currentDashboardBottomSheetFlavor.value = flavor
    }

    fun onSelectedClassManageClassChanged(item: ClassModel?) = effect {
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