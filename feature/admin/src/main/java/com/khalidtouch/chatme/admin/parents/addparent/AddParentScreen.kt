package com.khalidtouch.chatme.admin.parents.addparent

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.chatme.admin.parents.ParentScreenViewModel
import com.khalidtouch.classifiadmin.model.utils.OnCreateAccountState
import com.khalidtouch.core.designsystem.components.ClassifiTextButton


@Composable
fun AddParentScreen(
    windowSizeClass: WindowSizeClass,
    addParentViewModel: AddParentViewModel,
    parentScreenViewModel: ParentScreenViewModel,
    uiState: AddParentUiState = rememberAddParentUiState(windowSizeClass = windowSizeClass)
) {

    val TAG = "AddParent"
    val configuration = LocalConfiguration.current
    val state by addParentViewModel.state.collectAsStateWithLifecycle()
    val navController = (uiState as AddParentUiState.Success).data.navController
    val mySchool by addParentViewModel.observeMySchool.collectAsStateWithLifecycle()


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
                                if(state.canAddMoreParents) {
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
                                    result = { state, aborted ->
                                        Log.e(
                                            TAG,
                                            "AddParentScreen: ${aborted.size} Parents could not be saved"
                                        )
                                        when (state) {
                                            OnCreateAccountState.Success -> {
                                                navController.navigateToInputParentSuccessfulScreen(
                                                    addParentViewModel = addParentViewModel
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
        dismissButton = {},
        title = {},
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
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 16.dp)
    )
}