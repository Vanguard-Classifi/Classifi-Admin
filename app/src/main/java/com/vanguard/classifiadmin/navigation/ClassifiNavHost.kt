package com.vanguard.classifiadmin.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.khalidtouch.classifiadmin.feeds.compose.composeFeedScreen
import com.khalidtouch.classifiadmin.feeds.compose.navigateToComposeFeed
import com.khalidtouch.classifiadmin.feeds.takephoto.navigation.navigateToTakePhoto
import com.khalidtouch.classifiadmin.feeds.takephoto.navigation.takePhotoScreen
import com.khalidtouch.classifiadmin.settings.navigation.navigateToSettings
import com.khalidtouch.classifiadmin.settings.navigation.settingsScreen
import com.vanguard.classifiadmin.ui.ClassifiAppState
import com.vanguard.classifiadmin.ui.homeScreen
import com.vanguard.classifiadmin.ui.homeScreenNavigationRoute
import com.vanguard.classifiadmin.ui.navigateToHome
import com.vanguard.classifiadmin.ui.rememberClassifiAppState


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ClassifiNavHost(
    windowSizeClass: WindowSizeClass,
    appState: ClassifiAppState = rememberClassifiAppState(windowSizeClass = windowSizeClass),
    modifier: Modifier = Modifier,
    startDestination: String = homeScreenNavigationRoute,
) {
    AnimatedNavHost(
        navController = appState.navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeScreen(
            windowSizeClass = windowSizeClass,
            appState = appState,
            onComposeFeed = { appState.navController.navigateToComposeFeed() },
            onOpenSettings = { appState.navController.navigateToSettings() }
        )

        settingsScreen(
            windowSizeClass = windowSizeClass,
            onBack = { appState.navController.navigateUp() }
        )

        composeFeedScreen(
            onCloseComposeFeedScreen = { appState.navController.navigateUp() },
            onTakePhoto = { appState.navController.navigateToTakePhoto() }
        )

        takePhotoScreen(
            onDismissDialog = { appState.navController.navigateUp() },
            onClose = { appState.navController.navigateUp() },
            onNext = { appState.navController.navigateToComposeFeed() },
        )
    }
}