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
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.classifiadmin.model.utils.OnCreateAccountState
import com.khalidtouch.classifiadmin.model.utils.OnCreateBatchAccountResult
import com.khalidtouch.classifiadmin.model.utils.StagedUser
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel
import com.khalidtouch.core.designsystem.components.ClassifiTextButton


@Composable
fun AddParentDialog(
    windowSizeClass: WindowSizeClass,
    addParentViewModel: AddParentViewModel,
    parentScreenViewModel: ParentScreenViewModel,
    addParentUiState: AddParentUiState = rememberAddParentUiState(windowSizeClass = windowSizeClass)
) {
    val addParentState by addParentViewModel.state.collectAsStateWithLifecycle()
    val mySchool by addParentViewModel.observeMySchool.collectAsStateWithLifecycle()
    val registeringParentState by addParentViewModel.registeringParentState.collectAsStateWithLifecycle()
    val numberOfParentsRegistered by addParentViewModel.numberOfParentsRegistered.collectAsStateWithLifecycle()
    val numberOfParentsFailed by addParentViewModel.numberOfParentsFailed.collectAsStateWithLifecycle()
    val navController = (addParentUiState as AddParentUiState.Success).data.navController
    val unStagingEnabled by addParentViewModel.unStagingEnabled.collectAsStateWithLifecycle()

    AddParentDialog(
        progressBarState = registeringParentState.progressBarState,
        errorMessage = registeringParentState.message,
        snackbarState = registeringParentState.snackbarState,
        addParentState = addParentState,
        mySchool = mySchool,
        addParentUiState = addParentUiState,
        numberOfParentsRegistered = numberOfParentsRegistered,
        numberOfParentsFailed = numberOfParentsFailed,
        onCloseAddParentDialog = { parentScreenViewModel.updateAddParentDialogState(false) },
        onStageParent = addParentViewModel::onStageParent,
        onCreateAccountForParent = addParentViewModel::createAccountForParents,
        onShowProgressBar = { addParentViewModel.updateRegisteringParentsProgressBarState(true) },
        onHideProgressBar = { addParentViewModel.updateRegisteringParentsProgressBarState(false) },
        onShowSnackbar = { addParentViewModel.updateRegisteringParentsSnackbarState(true) },
        cacheNumberOfParentsRegistered = addParentViewModel::cacheNumberOfParentsRegistered,
        cacheNumberOfParentsFailed = addParentViewModel::cacheNumberOfParentsFailed,
        navigateToInputParentSuccessfulScreen = {
            navController.navigateToInputParentSuccessfulScreen(
                addParentViewModel
            )
        },
        onUpdateErrorMessage = addParentViewModel::updateRegisteringParentMessage,
        onHideAddParentDialog = { parentScreenViewModel.updateAddParentDialogState(false) },
        onEmailChanged = addParentViewModel::onEmailChanged,
        onPasswordChanged = addParentViewModel::onPasswordChanged,
        onConfirmPasswordChanged = addParentViewModel::onConfirmPasswordChanged,
        currentStagedUserId = registeringParentState.currentStagedUserId,
        unStagingEnabled = unStagingEnabled,
        unStageParent = { addParentViewModel.unStageParent(registeringParentState.currentStagedUserId) },
        updateFieldsWithCurrentUser = addParentViewModel::updateFieldsWithCurrentUser,
        updateCurrentStagedUserId = addParentViewModel::updateCurrentStagedUserId
    )
}

@Composable
private fun AddParentDialog(
    progressBarState: Boolean,
    errorMessage: String,
    snackbarState: Boolean,
    addParentState: AddParentState,
    mySchool: ClassifiSchool?,
    addParentUiState: AddParentUiState,
    numberOfParentsRegistered: Int,
    numberOfParentsFailed: Int,
    onCloseAddParentDialog: () -> Unit,
    onStageParent: (addParentState: AddParentState, school: ClassifiSchool?) -> Unit,
    onCreateAccountForParent: (
        school: ClassifiSchool?,
        stagedParents: List<StagedUser>,
        result: OnCreateBatchAccountResult
    ) -> Unit,
    onShowProgressBar: () -> Unit,
    onHideProgressBar: () -> Unit,
    onShowSnackbar: () -> Unit,
    cacheNumberOfParentsRegistered: (Int) -> Unit,
    cacheNumberOfParentsFailed: (Int) -> Unit,
    navigateToInputParentSuccessfulScreen: () -> Unit,
    onUpdateErrorMessage: (String) -> Unit,
    onHideAddParentDialog: () -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    currentStagedUserId: Long,
    unStagingEnabled: Boolean,
    unStageParent: () -> Unit,
    updateFieldsWithCurrentUser: (StagedUser) -> Unit,
    updateCurrentStagedUserId: (Long) -> Unit,
) {
    val TAG = "AddParent"
    val configuration = LocalConfiguration.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarState) {
        if (snackbarState) {
            snackbarHostState.showSnackbar(
                message = errorMessage,
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
                            enabled = addParentState.canSubmitParentsInfo,
                            onClick = {
                                when (addParentState.currentPage) {
                                    AddParentPage.SUCCESS -> {
                                        onCloseAddParentDialog()
                                    }

                                    AddParentPage.INPUT -> {
                                        //stage any trailing data
                                        if (addParentState.canAddMoreParents) {
                                            onStageParent(addParentState, mySchool)
                                        }
                                        onCreateAccountForParent(
                                            mySchool,
                                            addParentState.stagedParents,
                                        ) { state, users ->
                                            when (state) {
                                                OnCreateAccountState.Starting -> {
                                                    onShowProgressBar()
                                                }

                                                OnCreateAccountState.Success -> {
                                                    onHideProgressBar()
                                                    cacheNumberOfParentsRegistered(users.size)
                                                    navigateToInputParentSuccessfulScreen()
                                                }

                                                OnCreateAccountState.Failed -> {
                                                    onHideProgressBar()
                                                    cacheNumberOfParentsFailed(users.size)
                                                    onUpdateErrorMessage(
                                                        when (numberOfParentsFailed) {
                                                            0 -> "An error occurred"
                                                            1 -> "Sorry, could not register a Parent"
                                                            else -> "Sorry, could not register $numberOfParentsFailed Parents"
                                                        }
                                                    )
                                                    onShowSnackbar()
                                                }

                                                OnCreateAccountState.SchoolNotFound -> {
                                                    onHideProgressBar()
                                                    onUpdateErrorMessage(
                                                        "A school is needed to proceed with this operation"
                                                    )
                                                    onShowSnackbar()
                                                }

                                                else -> Unit
                                            }
                                        }
                                    }
                                }
                            },
                            text = {
                                val text = when (addParentState.currentPage) {
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
                        if (addParentState.currentPage == AddParentPage.INPUT) {
                            ClassifiTextButton(
                                enabled = true,
                                onClick = {
                                    onHideAddParentDialog()
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
                        addParentUiState = addParentUiState,
                        addParentState = addParentState,
                        mySchool = mySchool,
                        currentStagedUserId = currentStagedUserId,
                        unStagingEnabled = unStagingEnabled,
                        onEmailChanged = onEmailChanged,
                        onPasswordChanged = onPasswordChanged,
                        onConfirmPasswordChanged = onConfirmPasswordChanged,
                        onStageParent = onStageParent,
                        unStageParent = unStageParent,
                        updateCurrentStagedUserId = updateCurrentStagedUserId,
                        updateFieldsWithCurrentUser = updateFieldsWithCurrentUser,
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

    if (progressBarState) {
        ClassifiLoadingWheel()
    }
}