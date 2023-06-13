package com.khalidtouch.chatme.admin.parents.addparent

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddParentNavHost(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    addParentUiState: AddParentUiState,
    startDestination: String = inputParentInfoNavigationRoute,
    addParentViewModel: AddParentViewModel,
) {
    AnimatedNavHost(
        navController = (addParentUiState as AddParentUiState.Success).data.navController,
        startDestination = startDestination,
        modifier = modifier,
    ){
        inputParentInfo(
            addParentViewModel = addParentViewModel
        )

        inputParentSuccessfulScreen()
    }
}