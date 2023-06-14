package com.khalidtouch.chatme.admin.school.addschool

import android.util.Log
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.chatme.admin.school.SchoolViewModel
import com.khalidtouch.chatme.domain.usecases.OnRegisterSchoolState
import com.khalidtouch.core.designsystem.components.ClassifiTextButton

@Composable
fun AddSchoolScreen(
    schoolViewModel: SchoolViewModel,
    windowSizeClass: WindowSizeClass,
    addSchoolViewModel: AddSchoolViewModel,
    addSchoolUiState: AddSchoolUiState = rememberAddSchoolUiState(windowSizeClass = windowSizeClass),
) {
    val configuration = LocalConfiguration.current
    val state by addSchoolViewModel.state.collectAsStateWithLifecycle()

    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            Box(Modifier.padding(horizontal = 8.dp)) {
                ClassifiTextButton(
                    onClick = {
                        when (state.currentDestination) {
                            AddSchoolDestination.INPUT -> {
                                addSchoolViewModel.onRegisterSchoolToDb(
                                    info = AddSchoolInfo(
                                        userId = state.myId,
                                        name = state.schoolName,
                                        address = state.schoolAddress,
                                    )
                                ) {
                                    when (it) {
                                        is OnRegisterSchoolState.Starting -> Unit
                                        is OnRegisterSchoolState.Success -> {
                                            addSchoolUiState.navController.navigateToInputSchoolSuccessfulScreen(
                                                addSchoolViewModel = addSchoolViewModel,
                                            )
                                            addSchoolViewModel.onNavigate(AddSchoolDestination.SUCCESS)
                                        }

                                        is OnRegisterSchoolState.NameOrAddressError -> {

                                        }

                                        else -> Unit
                                    }
                                }
                            }

                            AddSchoolDestination.SUCCESS -> {
                                schoolViewModel.onHideAddSchoolDialog()
                                addSchoolViewModel.clearCurrentDestination()
                            }
                        }
                    },
                    text = {
                        val text = when (state.currentDestination) {
                            AddSchoolDestination.INPUT -> stringResource(id = R.string.save)
                            AddSchoolDestination.SUCCESS -> stringResource(id = R.string.finish)
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
            when (state.currentDestination) {
                AddSchoolDestination.INPUT -> {
                    Box(Modifier.padding(horizontal = 8.dp)) {
                        ClassifiTextButton(
                            onClick = {
                                schoolViewModel.onHideAddSchoolDialog()
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

                else -> Unit
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.add_school),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            AddSchoolNavHost(
                windowSizeClass = windowSizeClass,
                addSchoolUiState = addSchoolUiState,
                addSchoolViewModel = addSchoolViewModel,
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