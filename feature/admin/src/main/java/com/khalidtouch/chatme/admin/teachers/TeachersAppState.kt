package com.khalidtouch.chatme.admin.teachers

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope

@Stable
data class TeachersAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberTeachersAppState(
    navController: NavHostController = rememberAnimatedNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    windowSizeClass: WindowSizeClass,
): TeachersAppState {
    return TeachersAppState(
        navController = navController,
        coroutineScope = coroutineScope,
        windowSizeClass = windowSizeClass,
    )
}