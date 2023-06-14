package com.khalidtouch.chatme.admin.parents.addparent

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.classifiadmin.model.utils.StagedUser


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddParentNavHost(
    modifier: Modifier = Modifier,
    addParentUiState: AddParentUiState,
    addParentState: AddParentState,
    mySchool: ClassifiSchool?,
    currentStagedUserId: Long,
    unStagingEnabled: Boolean,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onStageParent: (addParentState: AddParentState, school: ClassifiSchool?) -> Unit,
    unStageParent: () -> Unit,
    updateFieldsWithCurrentUser: (StagedUser) -> Unit,
    updateCurrentStagedUserId: (Long) -> Unit,
    startDestination: String = inputParentInfoNavigationRoute,
) {
    AnimatedNavHost(
        navController = (addParentUiState as AddParentUiState.Success).data.navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        inputParentInfo(
            addParentState = addParentState,
            mySchool = mySchool,
            currentStagedUserId = currentStagedUserId,
            unStagingEnabled = unStagingEnabled,
            onEmailChanged = onEmailChanged,
            onPasswordChanged = onPasswordChanged,
            onConfirmPasswordChanged = onConfirmPasswordChanged,
            onStageParent = onStageParent,
            unStageParent = unStageParent,
            updateFieldsWithCurrentUser = updateFieldsWithCurrentUser,
            updateCurrentStagedUserId = updateCurrentStagedUserId,
        )

        inputParentSuccessfulScreen()
    }
}