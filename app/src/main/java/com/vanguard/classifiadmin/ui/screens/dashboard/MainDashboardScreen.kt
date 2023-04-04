package com.vanguard.classifiadmin.ui.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.AssessmentModel
import com.vanguard.classifiadmin.data.local.models.ClassModel
import com.vanguard.classifiadmin.data.local.models.FeedModel
import com.vanguard.classifiadmin.data.local.models.UserModel
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.router.BottomDestination
import com.vanguard.classifiadmin.router.BottomNavGraph
import com.vanguard.classifiadmin.ui.components.ClassFilterScreen
import com.vanguard.classifiadmin.ui.components.ClassifiFeature
import com.vanguard.classifiadmin.ui.components.CreateAssessmentBox
import com.vanguard.classifiadmin.ui.components.DashboardMenu
import com.vanguard.classifiadmin.ui.components.DashboardMenuScreen
import com.vanguard.classifiadmin.ui.components.FeatureListItem
import com.vanguard.classifiadmin.ui.components.Level
import com.vanguard.classifiadmin.ui.components.MessageBar
import com.vanguard.classifiadmin.ui.components.NoDataInline
import com.vanguard.classifiadmin.ui.components.StudentOption
import com.vanguard.classifiadmin.ui.components.StudentOptionsListItem
import com.vanguard.classifiadmin.ui.components.SuccessBar
import com.vanguard.classifiadmin.ui.screens.admin.CreateSubjectClassItem
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentState
import com.vanguard.classifiadmin.ui.screens.assessments.DraftAssessmentBottomSheetContent
import com.vanguard.classifiadmin.ui.screens.assessments.DraftAssessmentBottomSheetOption
import com.vanguard.classifiadmin.ui.screens.assessments.InReviewAssessmentBottomSheetContent
import com.vanguard.classifiadmin.ui.screens.assessments.InReviewAssessmentBottomSheetOption
import com.vanguard.classifiadmin.ui.screens.assessments.PublishedAssessmentBottomSheetContent
import com.vanguard.classifiadmin.ui.screens.assessments.PublishedAssessmentBottomSheetOption
import com.vanguard.classifiadmin.ui.screens.classes.JoinClassScreen
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val MAIN_DASHBOARD_SCREEN = "main_dashboard_screen"

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainDashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    goToFeature: (ClassifiFeature) -> Unit,
    onSelectMenu: (DashboardMenu) -> Unit,
    onSelectProfile: () -> Unit,
    onManageClass: (ClassModel) -> Unit,
    onLogin: () -> Unit = {},
    goToAssessmentReport: (AssessmentModel) -> Unit,
    goToAssessmentReview: (AssessmentModel) -> Unit,
    goToModifyAssessment: (AssessmentModel) -> Unit,
    onFeedDetail: (FeedModel) -> Unit,
    onStudentDetail: (UserModel) -> Unit,
    onCreateAssessment: () -> Unit,
    onViewReport: (FeedModel) -> Unit,
) {
    val navController = rememberNavController()
    val currentBottomSheetFlavor by viewModel.currentDashboardBottomSheetFlavor.collectAsState()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentUsernamePref by viewModel.currentUsernamePref.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val currentSchoolNamePref by viewModel.currentSchoolNamePref.collectAsState()
    val currentUserEmailPref by viewModel.currentUserEmailPref.collectAsState()
    val verifiedClassesNetwork by viewModel.verifiedClassesNetwork.collectAsState()
    val message by viewModel.dashboardMessage.collectAsState()
    val currentClassFeedPref by viewModel.currentClassFeedPref.collectAsState()
    val classByIdNetwork by viewModel.classByIdNetwork.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val showModalSheet = rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    var menuState by remember { mutableStateOf(false) }
    var filterState by remember { mutableStateOf(false) }
    var joinClassState by remember { mutableStateOf(false) }
    var createQuestionState by remember { mutableStateOf(false) }
    var verifiedClassesSorted = remember(verifiedClassesNetwork.data) {
        verifiedClassesNetwork.data?.sortedBy { it.className }
    }

    LaunchedEffect(Unit) {
        viewModel.getCurrentClassFeedPref()
        viewModel.getCurrentSchoolNamePref()
        viewModel.getCurrentSchoolIdPref()
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentUsernamePref()
        viewModel.getCurrentUserEmail()
        viewModel.getClassByIdNetwork(currentClassFeedPref.orEmpty(), currentSchoolIdPref.orEmpty())
        delay(3000)
        viewModel.clearSignInFields()
        viewModel.clearSignUpFields()
        viewModel.getVerifiedClassesNetwork(currentSchoolIdPref.orEmpty())
    }

    LaunchedEffect(currentClassFeedPref) {
        viewModel.getClassByIdNetwork(currentClassFeedPref.orEmpty(), currentSchoolIdPref.orEmpty())
    }

    LaunchedEffect(message) {
        if (message !is DashboardMessage.NoMessage) {
            delay(2000)
            viewModel.onDashboardMessageChanged(DashboardMessage.NoMessage)
        }
    }

    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        ModalBottomSheetLayout(
            modifier = modifier
                .width(maxWidth)
                .height(maxHeight),
            sheetState = sheetState,
            scrimColor = MaterialTheme.colors.primary.copy(0.3f),
            sheetElevation = 8.dp,
            sheetBackgroundColor = MaterialTheme.colors.onPrimary,
            sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            sheetContent = {
                DashboardBottomSheetContent(
                    modifier = modifier,
                    currentBottomSheetFlavor = currentBottomSheetFlavor
                        ?: DashboardBottomSheetFlavor.Features,
                    onSelectFeature = {
                        //hide bottom sheet
                        coroutineScope.launch {
                            showModalSheet.value = false
                            delay(500)
                            sheetState.hide()
                            goToFeature(it)          //go to feature destination goToFeature(it)          //go to feature destination
                        }
                    },
                    onSelectOption = {
                        /*TODO -> on student option pressed */
                        //hide bottom sheet
                        coroutineScope.launch {
                            showModalSheet.value = false
                            delay(500)
                            sheetState.hide()
                        }
                    },
                    onSelectDraftItem = {
                        /*TODO -> on draft option pressed */
                        //hide bottom sheet
                        coroutineScope.launch {
                            showModalSheet.value = false
                            delay(500)
                            sheetState.hide()
                        }
                    },
                    onSelectPublishedItem = {
                        /*TODO -> on draft option pressed */
                        //hide bottom sheet
                        coroutineScope.launch {
                            showModalSheet.value = false
                            delay(500)
                            sheetState.hide()
                        }
                    },
                    onSelectInReviewItem = {
                        /*TODO -> on draft option pressed */
                        //hide bottom sheet
                        coroutineScope.launch {
                            showModalSheet.value = false
                            delay(500)
                            sheetState.hide()
                        }
                    },
                    myClasses = verifiedClassesSorted?.map { it.toLocal() }
                        ?: emptyList<ClassModel>()
                )
            }
        ) {
            MainDashboardScreenContent(
                modifier = modifier,
                viewModel = viewModel,
                onLogin = onLogin,
                navController = navController,
                filterLabel = if (classByIdNetwork is Resource.Success && classByIdNetwork.data != null)
                    classByIdNetwork.data?.classCode.orEmpty() else "",
                filterState = filterState,
                onFilter = {
                    filterState = !filterState
                    //select class to show feeds
                    viewModel.onClassFilterModeChanged(ClassFilterMode.ReadFeeds)
                },
                openProfile = { menuState = !menuState },
                openSheet = {
                    viewModel.onCurrentDashboardBottomSheetFlavorChanged(
                        DashboardBottomSheetFlavor.Features
                    )
                    coroutineScope.launch {
                        showModalSheet.value = true
                        sheetState.show()
                    }
                },
                username = currentUsernamePref ?: "",
                onStudentOptions = {
                    viewModel.onCurrentDashboardBottomSheetFlavorChanged(
                        DashboardBottomSheetFlavor.StudentOptions
                    )
                    coroutineScope.launch {
                        showModalSheet.value = true
                        sheetState.show()
                    }
                },
                onSelectAssessment = {
                    //when assessment is published go to assessment report
                    when (it.state) {
                        AssessmentState.Published.name -> {
                            goToAssessmentReport(it)
                        }

                        AssessmentState.InReview.name -> {
                            goToAssessmentReview(it)
                        }

                        AssessmentState.Draft.name -> {
                            goToModifyAssessment(it)
                        }

                        else -> {
                            goToAssessmentReport(it)
                        }
                    }
                },
                onPublishedAssessmentOptions = {
                    viewModel.onCurrentDashboardBottomSheetFlavorChanged(
                        DashboardBottomSheetFlavor.PublishedAssessment
                    )
                    coroutineScope.launch {
                        showModalSheet.value = true
                        sheetState.show()
                    }
                },
                onInReviewAssessmentOptions = {
                    viewModel.onCurrentDashboardBottomSheetFlavorChanged(
                        DashboardBottomSheetFlavor.InReviewAssessment
                    )
                    coroutineScope.launch {
                        showModalSheet.value = true
                        sheetState.show()
                    }
                },
                onDraftAssessmentOptions = {
                    viewModel.onCurrentDashboardBottomSheetFlavorChanged(
                        DashboardBottomSheetFlavor.DraftAssessment
                    )
                    coroutineScope.launch {
                        showModalSheet.value = true
                        sheetState.show()
                    }
                },
                onSelectClasses = {
                    filterState = true
                    //select classes to post
                    viewModel.onClassFilterModeChanged(ClassFilterMode.Post)
                },
                onFeedDetail = onFeedDetail,
                onStudentDetail = onStudentDetail,
                onCreateQuestions = {
                    createQuestionState = true
                },
                onViewReport = onViewReport
            )
        }

        AnimatedVisibility(
            visible = menuState,
            enter = scaleIn(
                initialScale = 0.8f, animationSpec = tween(
                    durationMillis = 50, easing = FastOutLinearInEasing
                )
            ),
            exit = scaleOut(
                targetScale = 0.8f,
                animationSpec = tween(
                    durationMillis = 50, easing = FastOutLinearInEasing
                ),
            ),
        ) {
            Popup(alignment = Alignment.TopEnd,
                offset = IntOffset(0, 100),
                onDismissRequest = { menuState = false }) {
                DashboardMenuScreen(
                    username = currentUsernamePref ?: "",
                    email = currentUserEmailPref ?: "",
                    status = "Update your profile",
                    onSelectProfile = onSelectProfile,
                    onSelectMenu = onSelectMenu,
                )
            }
        }

        AnimatedVisibility(
            visible = filterState,
            enter = scaleIn(
                initialScale = 0.8f, animationSpec = tween(
                    durationMillis = 50, easing = FastOutLinearInEasing
                )
            ),
            exit = scaleOut(
                targetScale = 0.8f,
                animationSpec = tween(
                    durationMillis = 50, easing = FastOutLinearInEasing
                ),
            ),
        ) {
            Popup(alignment = Alignment.TopEnd,
                offset = IntOffset(0, 200),
                onDismissRequest = { filterState = false }) {
                ClassFilterScreen(
                    viewModel = viewModel,
                    onClose = { filterState = false },
                    onManageClass = onManageClass,
                    onAddClass = {
                        filterState = false
                    }
                )
            }
        }


        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            AnimatedVisibility(
                modifier = modifier.padding(top = 100.dp),
                visible = joinClassState,
                enter = scaleIn(
                    initialScale = 0.8f, animationSpec = tween(
                        durationMillis = 50, easing = FastOutLinearInEasing
                    )
                ),
                exit = scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(
                        durationMillis = 50, easing = FastOutLinearInEasing
                    ),
                ),
            ) {
                JoinClassScreen(
                    viewModel = viewModel,
                    onClose = {
                        joinClassState = false
                    },
                )
            }
        }


        if (message !is DashboardMessage.NoMessage) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                SuccessBar(
                    message = message.message,
                    maxWidth = maxWidth,
                    modifier = modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                )
            }
        }


        AnimatedVisibility(
            visible = createQuestionState,
            enter = scaleIn(
                initialScale = 0.95f, animationSpec = tween(
                    durationMillis = 20, easing = FastOutLinearInEasing
                )
            ),
            exit = scaleOut(
                targetScale = 0.95f,
                animationSpec = tween(
                    durationMillis = 20, easing = FastOutLinearInEasing
                ),
            ),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CreateAssessmentBox(
                    viewModel = viewModel,
                    onClose = { createQuestionState = false },
                    parentWidth = maxWidth,
                    onCreateAssessment = onCreateAssessment
                )
            }
        }

    }
}

@Composable
fun MainDashboardScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavHostController,
    filterLabel: String,
    filterState: Boolean,
    onFilter: (String) -> Unit,
    openSheet: () -> Unit,
    openProfile: () -> Unit,
    username: String,
    onStudentOptions: (UserModel) -> Unit,
    onStudentDetail: (UserModel) -> Unit,
    onLogin: () -> Unit = {},
    onPublishedAssessmentOptions: (AssessmentModel) -> Unit,
    onInReviewAssessmentOptions: (AssessmentModel) -> Unit,
    onDraftAssessmentOptions: (AssessmentModel) -> Unit,
    onSelectAssessment: (AssessmentModel) -> Unit,
    onSelectClasses: () -> Unit,
    onFeedDetail: (FeedModel) -> Unit,
    onCreateQuestions: () -> Unit,
    onViewReport: (FeedModel) -> Unit,
) {

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopBar(
                    filterLabel = filterLabel,
                    onFilter = onFilter,
                    openSheet = openSheet,
                    openProfile = openProfile,
                    username = username,
                    filterActivated = filterState,
                    onLogin = onLogin,
                )
            },
            bottomBar = {
                BottomBar(navController = navController)
            },
            content = { padding ->
                BottomContainer(
                    viewModel = viewModel,
                    navController = navController,
                    modifier = Modifier.padding(padding),
                    onStudentOptions = onStudentOptions,
                    onStudentDetail = onStudentDetail,
                    onSelectAssessment = onSelectAssessment,
                    onPublishedAssessmentOptions = onPublishedAssessmentOptions,
                    onDraftAssessmentOptions = onDraftAssessmentOptions,
                    onInReviewAssessmentOptions = onInReviewAssessmentOptions,
                    onSelectClasses = onSelectClasses,
                    onFeedDetail = onFeedDetail,
                    onCreateQuestions = onCreateQuestions,
                    onViewReport = onViewReport,
                )
            }
        )
    }
}

@Composable
fun DashboardBottomSheetContent(
    modifier: Modifier = Modifier,
    currentBottomSheetFlavor: DashboardBottomSheetFlavor,
    onSelectFeature: (ClassifiFeature) -> Unit,
    features: List<ClassifiFeature> = ClassifiFeature.values().toList(),
    studentOptions: List<StudentOption> = StudentOption.values().toList(),
    onSelectOption: (StudentOption) -> Unit,
    onSelectPublishedItem: (PublishedAssessmentBottomSheetOption) -> Unit,
    onSelectInReviewItem: (InReviewAssessmentBottomSheetOption) -> Unit,
    onSelectDraftItem: (DraftAssessmentBottomSheetOption) -> Unit,
    myClasses: List<ClassModel>,
) {

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(16.dp),
                color = Black100.copy(0.5f)
            ) {
                Box(
                    modifier = modifier
                        .width(102.dp)
                        .height(3.dp)
                )
            }
        }

        //swap content
        when (currentBottomSheetFlavor) {
            DashboardBottomSheetFlavor.Features -> {
                FeaturesBottomSheetContent(
                    modifier = modifier,
                    features = features,
                    onSelectFeature = onSelectFeature,
                )
            }

            DashboardBottomSheetFlavor.StudentOptions -> {
                StudentOptionsBottomSheetContent(
                    modifier = modifier,
                    studentOptions = studentOptions,
                    onSelectOption = onSelectOption,
                )
            }

            DashboardBottomSheetFlavor.DraftAssessment -> {
                DraftAssessmentBottomSheetContent(onSelectOption = onSelectDraftItem)
            }

            DashboardBottomSheetFlavor.InReviewAssessment -> {
                InReviewAssessmentBottomSheetContent(onSelectOption = onSelectInReviewItem)
            }

            DashboardBottomSheetFlavor.PublishedAssessment -> {
                PublishedAssessmentBottomSheetContent(
                    onSelectOption = onSelectPublishedItem,
                )
            }

            DashboardBottomSheetFlavor.AssignedClasses -> {
                AssignedClassesBottomSheetContent(
                    onSelectClass = {/*todo; on select class*/ },
                    myClasses = myClasses,
                )
            }

            else -> {
                FeaturesBottomSheetContent(
                    modifier = modifier,
                    features = features,
                    onSelectFeature = onSelectFeature,
                )
            }
        }

    }
}

@Composable
fun AssignedClassesBottomSheetContent(
    modifier: Modifier = Modifier,
    myClasses: List<ClassModel>,
    onSelectClass: (ClassModel) -> Unit,
) {
    if (myClasses.isEmpty()) {
        //no items screen
        NoDataInline(message = stringResource(id = R.string.classes_not_available))
    } else {
        LazyColumn(
            modifier = modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
            state = rememberLazyListState()
        ) {
            items(myClasses) { each ->
                CreateSubjectClassItem(
                    myClass = each,
                    onSelect = onSelectClass,
                )
            }
        }
    }
}


@Composable
fun StudentOptionsBottomSheetContent(
    modifier: Modifier = Modifier,
    studentOptions: List<StudentOption>,
    onSelectOption: (StudentOption) -> Unit,
) {
    LazyColumn(modifier = modifier.padding(bottom = 16.dp), state = rememberLazyListState()) {
        items(studentOptions) { each ->
            StudentOptionsListItem(studentOption = each, onSelect = onSelectOption)
        }
    }
}

@Composable
fun FeaturesBottomSheetContent(
    modifier: Modifier = Modifier,
    features: List<ClassifiFeature>,
    onSelectFeature: (ClassifiFeature) -> Unit,
) {
    LazyColumn(modifier = modifier.padding(bottom = 16.dp), state = rememberLazyListState()) {
        items(features) { each ->
            FeatureListItem(feature = each, onSelect = onSelectFeature)
        }
    }
}


enum class DestinationItem(val label: String, val icon: Int, val screen: String) {
    Feed("Feed", icon = R.drawable.icon_feeds, BottomDestination.feeds),
    Students("Students", R.drawable.icon_students, BottomDestination.students),
    Assessments("Assessments", R.drawable.icon_assessment, BottomDestination.assessments),
    Reports("Reports", R.drawable.icon_reports, BottomDestination.reports)
}

sealed class DashboardBottomSheetFlavor {
    object Features : DashboardBottomSheetFlavor()
    object StudentOptions : DashboardBottomSheetFlavor()
    object PublishedAssessment : DashboardBottomSheetFlavor()
    object InReviewAssessment : DashboardBottomSheetFlavor()
    object DraftAssessment : DashboardBottomSheetFlavor()
    object AssignedClasses : DashboardBottomSheetFlavor()
}

sealed class DashboardMessage(val message: String) {
    object FeedCreated : DashboardMessage("Successfully created a feed!")
    object NoMessage : DashboardMessage("")
}

sealed class ClassFilterMode {
    object Post : ClassFilterMode()
    object ReadFeeds : ClassFilterMode()
}