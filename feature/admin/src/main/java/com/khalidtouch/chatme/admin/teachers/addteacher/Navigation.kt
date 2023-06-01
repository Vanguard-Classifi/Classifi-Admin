package com.khalidtouch.chatme.admin.teachers.addteacher

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddTeacherNavHost(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    appState: AddTeacherUiState = rememberAddTeacherUiState(windowSize = windowSizeClass),
    startDestination: String = inputTeacherInfoNavigationRoute,
    addTeacherViewModel: AddTeacherViewModel,
) {
    AnimatedNavHost(
        navController = appState.navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        inputTeacherInfo(addTeacherViewModel = addTeacherViewModel)
    }
}