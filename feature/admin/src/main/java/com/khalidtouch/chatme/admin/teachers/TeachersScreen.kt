package com.khalidtouch.chatme.admin.teachers

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.navigation.animation.composable
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.chatme.admin.common.ItemNotAvailable
import com.khalidtouch.chatme.admin.teachers.addteacher.AddTeacherDialog
import com.khalidtouch.chatme.admin.teachers.addteacher.AddTeacherViewModel
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import com.khalidtouch.core.common.extensions.ifNullOrBlank
import com.khalidtouch.core.common.extensions.isEqualToDefaultImageUrl
import com.khalidtouch.core.common.extensions.orDefaultImageUrl
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel
import com.khalidtouch.core.designsystem.components.ClassifiBackground
import com.khalidtouch.core.designsystem.components.ClassifiFab
import com.khalidtouch.core.designsystem.components.ClassifiGradientBackground
import com.khalidtouch.core.designsystem.components.ClassifiSimpleTopAppBar
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.khalidtouch.core.designsystem.theme.LocalGradientColors
import com.khalidtouch.core.designsystem.ui.ClassifiUserThumbnail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TeachersRoute(
    onBackPressed: () -> Unit,
    teacherScreenViewModel: TeacherScreenViewModel,
    addTeacherViewModel: AddTeacherViewModel,
    windowSizeClass: WindowSizeClass,
    onOpenTeacherDetail: () -> Unit,
) {
    ClassifiBackground {
        val snackbarHostState = remember { SnackbarHostState() }
        val registeringTeacherState by addTeacherViewModel.registeringTeacherState.collectAsStateWithLifecycle()

        ClassifiGradientBackground(
            gradientColors = LocalGradientColors.current
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                containerColor = Color.Transparent,
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    val headerStyle = MaterialTheme.typography.titleMedium

                    ClassifiSimpleTopAppBar(
                        title = {
                            Box(Modifier.padding(start = 16.dp)) {
                                ProvideTextStyle(headerStyle) {
                                    Text(
                                        text = stringResource(id = R.string.manage_teachers)
                                    )
                                }
                            }
                        },
                        navIcon = {
                            Box(modifier = Modifier) {
                                IconButton(
                                    onClick = onBackPressed,
                                    enabled = true,
                                ) {
                                    Icon(
                                        painter = painterResource(id = ClassifiIcons.Back),
                                        contentDescription = stringResource(id = R.string.go_back)
                                    )
                                }
                            }
                        }
                    )
                },
                floatingActionButton = {
                    ClassifiFab(
                        onClick = { teacherScreenViewModel.updateAddTeacherDialogState(true) },
                        icon = {
                            Icon(
                                painter = painterResource(ClassifiIcons.Add),
                                contentDescription = stringResource(R.string.add_teacher)
                            )
                        }
                    )
                },
                content = {
                    TeachersScreen(
                        modifier = Modifier.padding(it),
                        teacherScreenViewModel = teacherScreenViewModel,
                        addTeacherViewModel = addTeacherViewModel,
                        windowSizeClass = windowSizeClass,
                        onOpenTeacherDetail = onOpenTeacherDetail,
                    )
                }
            )
        }
    }
}


@Composable
fun TeachersScreen(
    modifier: Modifier = Modifier,
    teacherScreenViewModel: TeacherScreenViewModel,
    addTeacherViewModel: AddTeacherViewModel,
    windowSizeClass: WindowSizeClass,
    onOpenTeacherDetail: () -> Unit,
) {
    val TAG = "TeacherScreen"
    val state = rememberLazyListState()
    val uiState by teacherScreenViewModel.uiState.collectAsStateWithLifecycle()
    val teacherSelectionListener by teacherScreenViewModel.teacherSelectionListener.collectAsStateWithLifecycle()


    when (uiState) {
        is TeacherScreenUiState.Loading -> Unit
        is TeacherScreenUiState.Success -> {
            val listOfTeachers =
                (uiState as TeacherScreenUiState.Success).data.listOfTeachers.collectAsLazyPagingItems()
            val hasFinishedLoading =
                (uiState as TeacherScreenUiState.Success).data.hasFinishedLoading
            val hasTeachers = (uiState as TeacherScreenUiState.Success).data.hasTeachers
            val selectedTeachers = (uiState as TeacherScreenUiState.Success).data.selectedTeachers
            val shouldShowExtraFeaturesOnTopBar =
                (uiState as TeacherScreenUiState.Success).data.shouldShowExtraFeaturesOnTopBar

            Log.e(TAG, "TeachersScreen: number of selected teachers ${selectedTeachers.size}")
            LaunchedEffect(teacherSelectionListener) {
                Log.e(TAG, "TeachersScreen: launchedeffect called")
                listOfTeachers.refresh()
            }

            LazyColumn(
                state = state,
                modifier = modifier
                    .fillMaxSize()
                    .testTag("admin:school"),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(listOfTeachers) { teacher ->
                    TeacherItem(
                        selected = selectedTeachers.contains(teacher?.userId),
                        teacher = checkNotNull(teacher),
                        onClick = {
                            if (shouldShowExtraFeaturesOnTopBar) {
                                teacherScreenViewModel.onSelectTeacherOrDeselect(it.userId ?: -1L)
                            } else {
                                teacherScreenViewModel.navigateToDetail(it.userId ?: -1L)
                                onOpenTeacherDetail()
                            }
                        },
                        onLongPress = {
                        },
                    )
                }
            }

            if (!hasTeachers) {
                ItemNotAvailable(
                    headerText = stringResource(id = R.string.no_teacher_added),
                    labelText = stringResource(id = R.string.click_plus_to_add)
                )
            }
        }
    }

    when (uiState) {
        is TeacherScreenUiState.Loading -> Unit
        is TeacherScreenUiState.Success -> {
            if ((uiState as TeacherScreenUiState.Success).data.shouldShowAddTeacherDialog) {
                AddTeacherDialog(
                    addTeacherViewModel = addTeacherViewModel,
                    teacherScreenViewModel = teacherScreenViewModel,
                    windowSizeClass = windowSizeClass,
                )
            }
        }
    }

    AnimatedVisibility(
        visible = uiState is TeacherScreenUiState.Success,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
        ) + fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            ClassifiLoadingWheel()
        }
    }
}


@Composable
private fun TeacherItem(
    teacher: ClassifiUser,
    selected: Boolean,
    onClick: (ClassifiUser) -> Unit,
    onLongPress: (ClassifiUser) -> Unit,
) {
    ClassifiUserThumbnail(
        username = teacher.account?.username.ifNullOrBlank(stringResource(id = R.string.name_not_specified)),
        email = teacher.account?.email.ifNullOrBlank(stringResource(id = R.string.email_not_added)),
        profileImage = teacher.profile?.profileImage.orDefaultImageUrl(),
        bio = teacher.profile?.bio.ifNullOrBlank(stringResource(id = R.string.empty_bio)),
        selected = selected,
        onClick = { onClick(teacher) },
        onLongPress = { onLongPress(teacher) },
        profileImageIsDefault = teacher.profile?.profileImage?.isEqualToDefaultImageUrl() ?: true
    )
}

const val teachersScreenNavigationRoute = "teachers_screen_navigation_route"

fun NavController.navigateToTeachersScreen() {
    this.navigate(teachersScreenNavigationRoute) {
        launchSingleTop = true
        popUpTo(teachersScreenNavigationRoute)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.teachersScreen(
    onBackPressed: () -> Unit,
    teacherScreenViewModel: TeacherScreenViewModel,
    addTeacherViewModel: AddTeacherViewModel,
    windowSizeClass: WindowSizeClass,
    onOpenTeacherDetail: () -> Unit,
) {
    composable(
        route = teachersScreenNavigationRoute,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(200)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(200)
            )
        }
    ) {
        TeachersRoute(
            onBackPressed = onBackPressed,
            teacherScreenViewModel = teacherScreenViewModel,
            addTeacherViewModel = addTeacherViewModel,
            windowSizeClass = windowSizeClass,
            onOpenTeacherDetail = onOpenTeacherDetail
        )
    }
}