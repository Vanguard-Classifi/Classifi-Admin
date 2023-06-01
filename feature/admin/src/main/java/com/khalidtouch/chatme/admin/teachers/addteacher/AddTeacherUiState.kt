package com.khalidtouch.chatme.admin.teachers.addteacher

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope

data class AddTeacherUiState(
    val navController: NavHostController,
    val windowSize: WindowSizeClass,
    val coroutineScope: CoroutineScope
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberAddTeacherUiState(
    navController: NavHostController = rememberAnimatedNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    windowSize: WindowSizeClass,
): AddTeacherUiState {
    return AddTeacherUiState(
        navController = navController,
        coroutineScope = coroutineScope,
        windowSize = windowSize,
    )
}