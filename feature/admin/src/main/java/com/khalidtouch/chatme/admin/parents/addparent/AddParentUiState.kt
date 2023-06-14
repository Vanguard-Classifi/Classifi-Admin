package com.khalidtouch.chatme.admin.parents.addparent

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope

sealed interface AddParentUiState {
    object Loading : AddParentUiState
    data class Success(val data: AddParentData) : AddParentUiState
}


data class AddParentData(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
    val coroutineScope: CoroutineScope
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberAddParentUiState(
    navController: NavHostController = rememberAnimatedNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    windowSizeClass: WindowSizeClass,
): AddParentUiState {
    return AddParentUiState.Success(
        data = AddParentData(
            navController = navController,
            coroutineScope = coroutineScope,
            windowSizeClass = windowSizeClass,
        )
    )
}