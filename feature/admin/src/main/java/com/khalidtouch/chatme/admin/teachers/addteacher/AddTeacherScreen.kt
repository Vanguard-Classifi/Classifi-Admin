package com.khalidtouch.chatme.admin.teachers.addteacher

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.chatme.admin.school.addschool.AddSchoolDestination
import com.khalidtouch.chatme.admin.school.addschool.AddSchoolInfo
import com.khalidtouch.chatme.admin.school.addschool.navigateToInputSchoolSuccessfulScreen
import com.khalidtouch.chatme.admin.teachers.TeacherScreenViewModel
import com.khalidtouch.chatme.domain.usecases.OnRegisterSchoolState
import com.khalidtouch.core.designsystem.components.ClassifiTextButton

@Composable
fun AddTeacherScreen(
    addTeacherViewModel: AddTeacherViewModel,
    teacherScreenViewModel: TeacherScreenViewModel,
    windowSizeClass: WindowSizeClass,
) {
    val configuration = LocalConfiguration.current
    val state by addTeacherViewModel.state.collectAsStateWithLifecycle()

    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            Box(Modifier.padding(horizontal = 8.dp)) {
                ClassifiTextButton(
                    enabled = state.canSubmitTeachersInfo,
                    onClick = {
                        when (state.currentPage) {
                            AddTeacherPage.SUCCESS -> {
                                teacherScreenViewModel.onSetAddTeacherDialogState(false)
                            }

                            AddTeacherPage.INPUT -> {
                                //todo -> register staged teachers
                                //then close dialog
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
                            teacherScreenViewModel.onSetAddTeacherDialogState(false)
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
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 16.dp)
    )
}