package com.khalidtouch.chatme.admin.school.addschool

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddSchoolNavHost(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    addSchoolUiState: AddSchoolUiState,
    addSchoolViewModel: AddSchoolViewModel,
    startDestination: String = inputSchoolInfoNavigationRoute,
) {
    AnimatedNavHost(
        navController = addSchoolUiState.navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        inputSchoolInfo(
            addSchoolViewModel = addSchoolViewModel,
        )
        inputSchoolSuccess()
    }
}