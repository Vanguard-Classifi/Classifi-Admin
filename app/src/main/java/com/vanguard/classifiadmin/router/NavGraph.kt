package com.vanguard.classifiadmin.router

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vanguard.classifiadmin.ui.screens.admin.CREATE_CLASS_ADMIN_SCREEN
import com.vanguard.classifiadmin.ui.screens.admin.CREATE_SUBJECT_ADMIN_SCREEN
import com.vanguard.classifiadmin.ui.screens.admin.CreateClassAdminScreen
import com.vanguard.classifiadmin.ui.screens.admin.CreateSubjectAdminScreen
import com.vanguard.classifiadmin.ui.screens.admin.ENROLL_PARENT_ADMIN_SCREEN
import com.vanguard.classifiadmin.ui.screens.admin.ENROLL_STUDENT_ADMIN_SCREEN
import com.vanguard.classifiadmin.ui.screens.admin.ENROLL_TEACHER_ADMIN_SCREEN
import com.vanguard.classifiadmin.ui.screens.admin.EnrollParentAdminScreen
import com.vanguard.classifiadmin.ui.screens.admin.EnrollStudentAdminScreen
import com.vanguard.classifiadmin.ui.screens.admin.EnrollTeacherAdminScreen
import com.vanguard.classifiadmin.ui.screens.admin.MANAGE_CLASS_ADMIN_DETAIL_SCREEN
import com.vanguard.classifiadmin.ui.screens.admin.MANAGE_CLASS_ADMIN_SCREEN
import com.vanguard.classifiadmin.ui.screens.admin.MANAGE_SUBJECT_ADMIN_SCREEN
import com.vanguard.classifiadmin.ui.screens.admin.ManageClassAdminDetailScreen
import com.vanguard.classifiadmin.ui.screens.admin.ManageClassAdminScreen
import com.vanguard.classifiadmin.ui.screens.admin.ManageSubjectAdminScreen
import com.vanguard.classifiadmin.ui.screens.assessments.ASSESSMENT_REPORT_SCREEN
import com.vanguard.classifiadmin.ui.screens.assessments.ASSESSMENT_REVIEW_SCREEN
import com.vanguard.classifiadmin.ui.screens.assessments.ASSESSMENT_SCREEN
import com.vanguard.classifiadmin.ui.screens.assessments.Assessment
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentReportScreen
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentReviewScreen
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentsScreen
import com.vanguard.classifiadmin.ui.screens.assessments.MODIFY_ASSESSMENT_SCREEN
import com.vanguard.classifiadmin.ui.screens.assessments.ModifyAssessmentScreen
import com.vanguard.classifiadmin.ui.screens.calendar.CALENDAR_SCREEN
import com.vanguard.classifiadmin.ui.screens.calendar.MyCalendarScreen
import com.vanguard.classifiadmin.ui.screens.classes.MANAGE_CLASS_SCREEN
import com.vanguard.classifiadmin.ui.screens.classes.ManageClassScreen
import com.vanguard.classifiadmin.ui.screens.dashboard.MAIN_DASHBOARD_SCREEN
import com.vanguard.classifiadmin.ui.screens.dashboard.MainDashboardScreen
import com.vanguard.classifiadmin.ui.screens.exportations.EXPORT_STUDENT_SCREEN
import com.vanguard.classifiadmin.ui.screens.exportations.EXPORT_SUBJECT_SCREEN
import com.vanguard.classifiadmin.ui.screens.exportations.EXPORT_TEACHER_SCREEN
import com.vanguard.classifiadmin.ui.screens.exportations.ExportStudentScreen
import com.vanguard.classifiadmin.ui.screens.exportations.ExportSubjectScreen
import com.vanguard.classifiadmin.ui.screens.exportations.ExportTeacherScreen
import com.vanguard.classifiadmin.ui.screens.feeds.FEEDS_SCREEN
import com.vanguard.classifiadmin.ui.screens.feeds.FeedsScreen
import com.vanguard.classifiadmin.ui.screens.importations.IMPORT_STUDENT_SCREEN
import com.vanguard.classifiadmin.ui.screens.importations.IMPORT_SUBJECT_SCREEN
import com.vanguard.classifiadmin.ui.screens.importations.IMPORT_TEACHER_SCREEN
import com.vanguard.classifiadmin.ui.screens.importations.ImportStudentScreen
import com.vanguard.classifiadmin.ui.screens.importations.ImportSubjectScreen
import com.vanguard.classifiadmin.ui.screens.importations.ImportTeacherScreen
import com.vanguard.classifiadmin.ui.screens.profile.ACCOUNT_SCREEN
import com.vanguard.classifiadmin.ui.screens.profile.MyAccountScreen
import com.vanguard.classifiadmin.ui.screens.profile.STUDENT_PROFILE_SCREEN
import com.vanguard.classifiadmin.ui.screens.profile.StudentProfileScreen
import com.vanguard.classifiadmin.ui.screens.reports.REPORTS_SCREEN
import com.vanguard.classifiadmin.ui.screens.reports.ReportsScreen
import com.vanguard.classifiadmin.ui.screens.results.STUDENT_RESULTS_SCREEN
import com.vanguard.classifiadmin.ui.screens.results.StudentResultsScreen
import com.vanguard.classifiadmin.ui.screens.students.STUDENTS_SCREEN
import com.vanguard.classifiadmin.ui.screens.students.StudentsScreen
import com.vanguard.classifiadmin.ui.screens.support.SUPPORT_SCREEN
import com.vanguard.classifiadmin.ui.screens.support.SupportScreen
import com.vanguard.classifiadmin.ui.screens.weeklyplan.WEEKLY_PLAN_DETAIL_SCREEN
import com.vanguard.classifiadmin.ui.screens.weeklyplan.WEEKLY_PLAN_SCREEN
import com.vanguard.classifiadmin.ui.screens.weeklyplan.WeeklyPlanDetailScreen
import com.vanguard.classifiadmin.ui.screens.weeklyplan.WeeklyPlanScreen
import com.vanguard.classifiadmin.ui.screens.welcome.ADD_SCHOOL_SCREEN
import com.vanguard.classifiadmin.ui.screens.welcome.AddSchoolScreen
import com.vanguard.classifiadmin.ui.screens.welcome.CREATE_SCHOOL_SCREEN
import com.vanguard.classifiadmin.ui.screens.welcome.CreateSchoolScreen
import com.vanguard.classifiadmin.ui.screens.welcome.LOGIN_SCREEN
import com.vanguard.classifiadmin.ui.screens.welcome.LoginScreen
import com.vanguard.classifiadmin.viewmodel.MainViewModel

object Destinations {
    const val dashboard = MAIN_DASHBOARD_SCREEN
    const val studentResults = STUDENT_RESULTS_SCREEN
    const val support = SUPPORT_SCREEN
    const val calendar = CALENDAR_SCREEN
    const val account = ACCOUNT_SCREEN
    const val manageClass = MANAGE_CLASS_SCREEN
    const val studentProfile = STUDENT_PROFILE_SCREEN
    const val assessmentReport = ASSESSMENT_REPORT_SCREEN
    const val assessmentReview = ASSESSMENT_REVIEW_SCREEN
    const val modifyAssessment = MODIFY_ASSESSMENT_SCREEN
    const val weeklyPlan = WEEKLY_PLAN_SCREEN
    const val weeklyPlanDetail = WEEKLY_PLAN_DETAIL_SCREEN
    const val login = LOGIN_SCREEN
    const val createSchool = CREATE_SCHOOL_SCREEN
    const val addSchoolScreen = ADD_SCHOOL_SCREEN
    const val createClassAdmin = CREATE_CLASS_ADMIN_SCREEN
    const val createSubjectAdmin = CREATE_SUBJECT_ADMIN_SCREEN
    const val enrollTeacherAdmin = ENROLL_TEACHER_ADMIN_SCREEN
    const val enrollStudentAdmin = ENROLL_STUDENT_ADMIN_SCREEN
    const val enrollParentAdmin = ENROLL_PARENT_ADMIN_SCREEN
    const val manageClassAdmin = MANAGE_CLASS_ADMIN_SCREEN
    const val manageSubjectAdmin = MANAGE_SUBJECT_ADMIN_SCREEN
    const val manageClassAdminDetail = MANAGE_CLASS_ADMIN_DETAIL_SCREEN
    const val importSubject = IMPORT_SUBJECT_SCREEN
    const val importTeacher = IMPORT_TEACHER_SCREEN
    const val importStudent = IMPORT_STUDENT_SCREEN
    const val exportTeacher = EXPORT_TEACHER_SCREEN
    const val exportSubject = EXPORT_SUBJECT_SCREEN
    const val exportStudent = EXPORT_STUDENT_SCREEN
}


@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    finishActivity: () -> Unit,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destinations.dashboard,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Destinations.dashboard) {
            MainDashboardScreen(
                viewModel = viewModel,
                goToFeature = { navController.navigate(it.screen) },
                onSelectMenu = { navController.navigate(it.screen) },
                onSelectProfile = { navController.navigate(Destinations.account) },
                onManageClass = { navController.navigate(Destinations.manageClass) },
                goToAssessmentReport = { navController.navigate(Destinations.assessmentReport) },
                goToAssessmentReview = { navController.navigate(Destinations.assessmentReview) },
                goToModifyAssessment = { navController.navigate(Destinations.modifyAssessment) },
                onLogin = { navController.navigate(Destinations.login) }
            )
        }

        composable(Destinations.studentResults) {
            StudentResultsScreen(
                viewModel = viewModel
            )
        }

        composable(Destinations.support) {
            SupportScreen()
        }

        composable(Destinations.calendar) {
            MyCalendarScreen()
        }

        composable(Destinations.account) {
            MyAccountScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.dashboard) },
                onAdminManageSubject = { navController.navigate(Destinations.manageSubjectAdmin) },
                onAdminManageClasses = { navController.navigate(Destinations.manageClassAdmin) },
                onAdminCreateSubjects = { navController.navigate(Destinations.createSubjectAdmin) },
                onAdminCreateClasses = { navController.navigate(Destinations.createClassAdmin) },
                onAdminEnrollTeacher = { navController.navigate(Destinations.enrollTeacherAdmin) },
                onAdminEnrollStudent = { navController.navigate(Destinations.enrollStudentAdmin) },
                onAdminEnrollParent = { navController.navigate(Destinations.enrollParentAdmin) },
            )
        }

        composable(Destinations.manageClass) {
            ManageClassScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.dashboard) }
            )
        }

        composable(Destinations.studentProfile) {
            StudentProfileScreen(
                viewModel = viewModel,
            )
        }

        composable(Destinations.assessmentReport) {
            AssessmentReportScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.dashboard) },
            )
        }

        composable(Destinations.assessmentReview) {
            AssessmentReviewScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.dashboard) }
            )
        }

        composable(Destinations.modifyAssessment) {
            ModifyAssessmentScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.dashboard) }
            )
        }

        composable(Destinations.weeklyPlan) {
            WeeklyPlanScreen(
                viewModel = viewModel
            )
        }

        composable(Destinations.weeklyPlanDetail) {
            WeeklyPlanDetailScreen(
                viewModel = viewModel
            )
        }

        composable(Destinations.login) {
            LoginScreen(
                viewModel = viewModel,
                onCreateSchool = { navController.navigate(Destinations.createSchool) },
                onLoginCompleted = { navController.navigate(Destinations.dashboard) },
                onAddSchool = { navController.navigate(Destinations.addSchoolScreen) },
            )
        }

        composable(Destinations.createSchool) {
            CreateSchoolScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.login) },
                onAddSchool = { navController.navigate(Destinations.login) },
                onSignUpCompleted = { navController.navigate(Destinations.dashboard) }
            )
        }

        composable(Destinations.addSchoolScreen) {
            AddSchoolScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.login) },
                onSchoolCreated = { navController.navigate(Destinations.dashboard) },
                onJoinSchool = { navController.navigate(Destinations.login) }
            )
        }

        composable(Destinations.createClassAdmin) {
            CreateClassAdminScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.account) }
            )
        }

        composable(Destinations.createSubjectAdmin) {
            CreateSubjectAdminScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.account) }
            )
        }

        composable(Destinations.enrollParentAdmin) {
            EnrollParentAdminScreen(
                onBack = { navController.navigate(Destinations.account) },
                viewModel = viewModel
            )
        }

        composable(Destinations.enrollTeacherAdmin) {
            EnrollTeacherAdminScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.account) }
            )
        }

        composable(Destinations.enrollStudentAdmin) {
            EnrollStudentAdminScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.account) }
            )
        }

        composable(Destinations.manageClassAdmin) {
            ManageClassAdminScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.account) },
                onManageClassDetail = { navController.navigate(Destinations.manageClassAdminDetail) }
            )
        }

        composable(Destinations.manageSubjectAdmin) {
            ManageSubjectAdminScreen(
            )
        }

        composable(Destinations.manageClassAdminDetail) {
            ManageClassAdminDetailScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.manageClassAdmin) },
                onImportSubject = { navController.navigate(Destinations.importSubject) },
                onInviteTeachers = { navController.navigate(Destinations.importTeacher) },
                onImportStudent = { navController.navigate(Destinations.importStudent) },
                onEnrollTeacher = { navController.navigate(Destinations.enrollTeacherAdmin) },
                onExportTeacher = { navController.navigate(Destinations.exportTeacher) },
                onExportStudent = { navController.navigate(Destinations.exportStudent) },
                onExportSubject = { navController.navigate(Destinations.exportSubject) },
                onEnrollStudent = { navController.navigate(Destinations.enrollStudentAdmin) }
            )
        }

        composable(Destinations.importSubject) {
            ImportSubjectScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.manageClassAdminDetail) },
                onSubjectImported = { navController.navigate(Destinations.manageClassAdminDetail) },
                onAddSubject = { navController.navigate(Destinations.createSubjectAdmin) }
            )
        }

        composable(Destinations.importStudent) {
            ImportStudentScreen(
                onBack = { navController.navigate(Destinations.manageClassAdminDetail) },
                viewModel = viewModel,
                onEnrollStudent = { navController.navigate(Destinations.enrollStudentAdmin) },
                onStudentImported = { navController.navigate(Destinations.manageClassAdminDetail) }
            )
        }

        composable(Destinations.importTeacher) {
            ImportTeacherScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.manageClassAdminDetail) },
                onEnrollTeacher = { navController.navigate(Destinations.enrollTeacherAdmin) },
                onTeacherImported = { navController.navigate(Destinations.manageClassAdminDetail) }
            )
        }

        composable(Destinations.exportTeacher) {
            ExportTeacherScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.manageClassAdminDetail) },
                onTeacherExported = { navController.navigate(Destinations.manageClassAdminDetail) }
            )
        }


        composable(Destinations.exportSubject) {
            ExportSubjectScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.manageClassAdminDetail) },
                onSubjectExported = { navController.navigate(Destinations.manageClassAdminDetail) }
            )
        }

        composable(Destinations.exportStudent) {
            ExportStudentScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(Destinations.manageClassAdminDetail) },
            )
        }
    }
}


object BottomDestination {
    const val feeds = FEEDS_SCREEN
    const val students = STUDENTS_SCREEN
    const val assessments = ASSESSMENT_SCREEN
    const val reports = REPORTS_SCREEN
}

@Composable
fun BottomNavGraph(
    modifier: Modifier = Modifier,
    startDestination: String = BottomDestination.feeds,
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel,
    onStudentOptions: () -> Unit,
    onPublishedAssessmentOptions: (Assessment) -> Unit,
    onInReviewAssessmentOptions: (Assessment) -> Unit,
    onDraftAssessmentOptions: (Assessment) -> Unit,
    onSelectAssessment: (Assessment) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        //feeds
        composable(BottomDestination.feeds) {
            FeedsScreen(
                modifier = modifier,
                viewModel = viewModel,
            )
        }

        //students
        composable(BottomDestination.students) {
            StudentsScreen(
                modifier = modifier,
                viewModel = viewModel,
                onStudentOptions = onStudentOptions,
            )
        }

        //assessments
        composable(BottomDestination.assessments) {
            AssessmentsScreen(
                modifier = modifier,
                viewModel = viewModel,
                onSelectAssessment = onSelectAssessment,
                onPublishedAssessmentOptions = onPublishedAssessmentOptions,
                onInReviewAssessmentOptions = onInReviewAssessmentOptions,
                onDraftAssessmentOptions = onDraftAssessmentOptions,
            )
        }

        //reports
        composable(BottomDestination.reports) {
            ReportsScreen(
                modifier = modifier,
                viewModel = viewModel,
            )
        }

    }
}