package com.khalidtouch.chatme.admin.school.addschool

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope


data class AddSchoolUiState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
    val coroutineScope: CoroutineScope
)


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberAddSchoolUiState(
    navController: NavHostController = rememberAnimatedNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    windowSizeClass: WindowSizeClass
): AddSchoolUiState {
    return AddSchoolUiState(
        navController = navController,
        coroutineScope = coroutineScope,
        windowSizeClass = windowSizeClass,
    )
}