package com.khalidtouch.chatme.admin.teachers.addteacher

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.chatme.admin.teachers.TeacherScreenViewModel
import com.khalidtouch.classifiadmin.model.utils.OnCreateAccountState
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel
import com.khalidtouch.core.designsystem.components.ClassifiTextButton

@Composable
fun AddTeacherDialog(
    addTeacherViewModel: AddTeacherViewModel,
    teacherScreenViewModel: TeacherScreenViewModel,
    windowSizeClass: WindowSizeClass,
    uiState: AddTeacherUiState = rememberAddTeacherUiState(windowSize = windowSizeClass),
) {
    val TAG = "AddTeacher"
    val configuration = LocalConfiguration.current
    val state by addTeacherViewModel.state.collectAsStateWithLifecycle()
    val navController = (uiState as AddTeacherUiState.Success).data.navController
    val mySchool by addTeacherViewModel.observeMySchool.collectAsStateWithLifecycle()
    val registeringTeacherState by addTeacherViewModel.registeringTeacherState.collectAsStateWithLifecycle()
    val numberOfTeachersRegistered by addTeacherViewModel.numberOfTeachersRegistered.collectAsStateWithLifecycle()
    val numberOfTeachersFailed by addTeacherViewModel.numberOfTeachersFailed.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(registeringTeacherState) {
        if(registeringTeacherState.snackbarState) {
            snackbarHostState.showSnackbar(
                message = registeringTeacherState.message,
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost( snackbarHostState ) },
        content = {
            AlertDialog(
                onDismissRequest = { },
                confirmButton = {
                    Box(Modifier.padding(horizontal = 8.dp)) {
                        ClassifiTextButton(
                            enabled = state.canSubmitTeachersInfo,
                            onClick = {
                                when (state.currentPage) {
                                    AddTeacherPage.SUCCESS -> {
                                        teacherScreenViewModel.updateAddTeacherDialogState(false)
                                    }

                                    AddTeacherPage.INPUT -> {
                                        //stage any trailing data
                                        if (state.canAddMoreTeachers) {
                                            addTeacherViewModel.onStageTeacher(
                                                email = state.email,
                                                password = state.password,
                                                confirmPassword = state.confirmPassword,
                                                mySchool = mySchool,
                                            )
                                        }
                                        addTeacherViewModel.createAccountForTeachers(
                                            teachers = state.stagedTeachers,
                                            mySchool = mySchool,
                                            result = { state, users ->
                                                when (state) {
                                                    OnCreateAccountState.Starting -> {
                                                        addTeacherViewModel.updateRegisteringTeachersProgressBarState(
                                                            true
                                                        )
                                                    }

                                                    OnCreateAccountState.Success -> {
                                                        addTeacherViewModel.updateRegisteringTeachersProgressBarState(
                                                            false
                                                        )
                                                        addTeacherViewModel.cacheNumberOfTeachersRegistered(
                                                            users.size
                                                        )
                                                        navController.navigateToInputTeacherSuccessfulScreen(
                                                            addTeacherViewModel = addTeacherViewModel
                                                        )
                                                    }

                                                    OnCreateAccountState.Failed -> {
                                                        addTeacherViewModel.updateRegisteringTeachersProgressBarState(
                                                            false
                                                        )
                                                        addTeacherViewModel.cacheNumberOfTeachersFailed(
                                                            users.size
                                                        )
                                                        addTeacherViewModel.updateRegisteringTeacherMessage(
                                                            when (numberOfTeachersFailed) {
                                                                0 -> "An error occurred"
                                                                1 -> "Sorry, could not register a teacher"
                                                                else -> "Sorry, could not register $numberOfTeachersFailed teachers"
                                                            }
                                                        )
                                                        addTeacherViewModel.updateRegisteringTeachersSnackbarState(
                                                            true
                                                        )
                                                    }

                                                    OnCreateAccountState.SchoolNotFound -> {
                                                        addTeacherViewModel.updateRegisteringTeachersProgressBarState(
                                                            false
                                                        )
                                                        addTeacherViewModel.updateRegisteringTeacherMessage(
                                                            "A school is needed to proceed with this operation"
                                                        )
                                                        addTeacherViewModel.updateRegisteringTeachersSnackbarState(
                                                            true
                                                        )
                                                    }

                                                    else -> Unit
                                                }
                                            },
                                        )
                                    }
                                }
                            },
                            text = {
                                val text = when (state.currentPage) {
                                    AddTeacherPage.INPUT -> stringResource(id = R.string.submit)
                                    AddTeacherPage.SUCCESS -> stringResource(id = R.string.finish)
                                }

                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        )
                    }
                },
                dismissButton = {
                    if (state.currentPage == AddTeacherPage.INPUT) {
                        Box(Modifier.padding(horizontal = 8.dp)) {
                            ClassifiTextButton(
                                onClick = {
                                    teacherScreenViewModel.updateAddTeacherDialogState(false)
                                },
                                text = {
                                    val text = stringResource(id = R.string.cancel)
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            )
                        }
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.add_teachers),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    AddTeacherNavHost(
                        appState = uiState,
                        windowSizeClass = windowSizeClass,
                        addTeacherViewModel = addTeacherViewModel,
                    )
                },
                tonalElevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                properties = DialogProperties(
                    usePlatformDefaultWidth = false,
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false,
                ),
                modifier = Modifier
                    .widthIn(max = configuration.screenWidthDp.dp - 16.dp)
                    .padding(it)
            )

        }
    )

    if (registeringTeacherState.progressBarState) {
        ClassifiLoadingWheel()
    }

}