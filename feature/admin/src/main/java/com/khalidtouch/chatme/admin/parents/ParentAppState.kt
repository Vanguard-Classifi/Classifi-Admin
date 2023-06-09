package com.khalidtouch.chatme.admin.parents

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope

@Stable
data class ParentAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberParentAppState(
    navController: NavHostController = rememberAnimatedNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    windowSizeClass: WindowSizeClass,
) : ParentAppState {
    return ParentAppState(
        navController = navController,
        coroutineScope = coroutineScope,
        windowSizeClass = windowSizeClass
    )
}

