package com.khalidtouch.chatme.admin.parents.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.khalidtouch.chatme.admin.parents.ParentAppState
import com.khalidtouch.chatme.admin.parents.ParentScreenViewModel
import com.khalidtouch.chatme.admin.parents.addparent.AddParentViewModel
import com.khalidtouch.chatme.admin.parents.parentsScreen
import com.khalidtouch.chatme.admin.parents.parentsScreenNavigationRoute
import com.khalidtouch.chatme.admin.parents.rememberParentAppState

const val parentsAdminPanelNavigationRoute = "parent_admin_panel_navigation_route"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.parentAdminPanel(
    windowSizeClass: WindowSizeClass,
    onBackPressed: () -> Unit,
) {
    composable(
        route = parentsAdminPanelNavigationRoute,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(200)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(200)
            )
        }
    ) {
        ParentAdminPanel(
            windowSizeClass = windowSizeClass,
            onBackPressed = onBackPressed,
        )
    }
}

fun NavController.navigateToParentAdminPanel(navOptions: NavOptions? = null) {
    this.navigate(parentsAdminPanelNavigationRoute, navOptions)
}


@Composable
private fun ParentAdminPanel(
    windowSizeClass: WindowSizeClass,
    onBackPressed: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        ParentNavHost(
            windowSizeClass = windowSizeClass,
            onBackPressed = onBackPressed,
        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ParentNavHost(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    onBackPressed: () -> Unit,
    addParentViewModel: AddParentViewModel = hiltViewModel<AddParentViewModel>(),
    parentScreenViewModel: ParentScreenViewModel = hiltViewModel<ParentScreenViewModel>(),
    startDestination: String = parentsScreenNavigationRoute,
    appState: ParentAppState = rememberParentAppState(windowSizeClass = windowSizeClass),
){
    AnimatedNavHost(
        navController = appState.navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        parentsScreen(
            windowSizeClass = windowSizeClass,
            onBackPressed = onBackPressed,
            parentScreenViewModel = parentScreenViewModel,
            addParentViewModel = addParentViewModel,
        )
    }
}