package com.khalidtouch.chatme.admin.parents.addparent

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
import com.khalidtouch.chatme.admin.parents.ParentScreenViewModel
import com.khalidtouch.classifiadmin.model.utils.OnCreateAccountState
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel
import com.khalidtouch.core.designsystem.components.ClassifiTextButton


@Composable
fun AddParentDialog(
    windowSizeClass: WindowSizeClass,
    addParentViewModel: AddParentViewModel,
    parentScreenViewModel: ParentScreenViewModel,
    uiState: AddParentUiState = rememberAddParentUiState(windowSizeClass = windowSizeClass)
) {
    val state by addParentViewModel.state.collectAsStateWithLifecycle()
    val mySchool by addParentViewModel.observeMySchool.collectAsStateWithLifecycle()
    val registeringParentState by addParentViewModel.registeringParentState.collectAsStateWithLifecycle()
    val numberOfParentsRegistered by addParentViewModel.numberOfParentsRegistered.collectAsStateWithLifecycle()
    val numberOfParentsFailed by addParentViewModel.numberOfParentsFailed.collectAsStateWithLifecycle()

    AddParentDialog()
}

@Composable
private fun AddParentDialog(
    windowSizeClass: WindowSizeClass,
    progressBarState: Boolean,
    addParentState: AddParentState,
    addParentUiState: AddParentUiState,
) {
    val TAG = "AddParent"
    val configuration = LocalConfiguration.current

    val navController = (addParentUiState as AddParentUiState.Success).data.navController


    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(registeringParentState) {
        if (registeringParentState.snackbarState) {
            snackbarHostState.showSnackbar(
                message = registeringParentState.message,
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )
            /*todo -> clear snackbar state */
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    Box(Modifier.padding(horizontal = 8.dp)) {
                        ClassifiTextButton(
                            enabled = state.canSubmitParentsInfo,
                            onClick = {
                                when (state.currentPage) {
                                    AddParentPage.SUCCESS -> {
                                        parentScreenViewModel.updateAddParentDialogState(false)
                                    }

                                    AddParentPage.INPUT -> {
                                        //stage any trailing data
                                        if (state.canAddMoreParents) {
                                            addParentViewModel.onStageParent(
                                                email = state.email,
                                                password = state.password,
                                                confirmPassword = state.confirmPassword,
                                                mySchool = mySchool,
                                            )
                                        }
                                        addParentViewModel.createAccountForParents(
                                            parents = state.stagedParents,
                                            mySchool = mySchool,
                                            result = { state, users ->
                                                when (state) {
                                                    OnCreateAccountState.Starting -> {
                                                        addParentViewModel.updateRegisteringParentsProgressBarState(
                                                            true
                                                        )
                                                    }

                                                    OnCreateAccountState.Success -> {
                                                        addParentViewModel.updateRegisteringParentsProgressBarState(
                                                            false
                                                        )
                                                        addParentViewModel.cacheNumberOfParentsRegistered(
                                                            users.size
                                                        )
                                                        navController.navigateToInputParentSuccessfulScreen(
                                                            addParentViewModel = addParentViewModel
                                                        )
                                                    }

                                                    OnCreateAccountState.Failed -> {
                                                        addParentViewModel.updateRegisteringParentsProgressBarState(
                                                            false
                                                        )
                                                        addParentViewModel.cacheNumberOfParentsFailed(
                                                            users.size
                                                        )
                                                        addParentViewModel.updateRegisteringParentMessage(
                                                            when (numberOfParentsFailed) {
                                                                0 -> "An error occurred"
                                                                1 -> "Sorry, could not register a Parent"
                                                                else -> "Sorry, could not register $numberOfParentsFailed Parents"
                                                            }
                                                        )
                                                        addParentViewModel.updateRegisteringParentsSnackbarState(
                                                            true
                                                        )
                                                    }

                                                    OnCreateAccountState.SchoolNotFound -> {
                                                        addParentViewModel.updateRegisteringParentsProgressBarState(
                                                            false
                                                        )
                                                        addParentViewModel.updateRegisteringParentMessage(
                                                            "A school is needed to proceed with this operation"
                                                        )
                                                        addParentViewModel.updateRegisteringParentsSnackbarState(
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
                                    AddParentPage.INPUT -> stringResource(id = R.string.submit)
                                    AddParentPage.SUCCESS -> stringResource(id = R.string.finish)
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
                    Box(Modifier.padding(horizontal = 8.dp)) {
                        if (state.currentPage == AddParentPage.INPUT) {
                            ClassifiTextButton(
                                enabled = true,
                                onClick = {
                                    parentScreenViewModel.updateAddParentDialogState(false)
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
                        text = stringResource(id = R.string.add_parent),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    AddParentNavHost(
                        windowSizeClass = windowSizeClass,
                        addParentUiState = uiState,
                        addParentViewModel = addParentViewModel
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

    if (registeringParentState.progressBarState) {
        ClassifiLoadingWheel()
    }
}