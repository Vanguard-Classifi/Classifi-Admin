package com.vanguard.classifiadmin.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.khalidtouch.chatme.admin.school.navigation.navigateToSchoolAdminPanel
import com.khalidtouch.chatme.admin.school.navigation.schoolAdminPanel
import com.khalidtouch.chatme.admin.teachers.navigation.navigateToTeachersAdminPanel
import com.khalidtouch.chatme.admin.teachers.navigation.teachersAdminPanel
import com.khalidtouch.classifiadmin.feeds.compose.composeFeedScreen
import com.khalidtouch.classifiadmin.feeds.compose.navigateToComposeFeed
import com.khalidtouch.classifiadmin.feeds.mediastore.imageGalleryScreen
import com.khalidtouch.classifiadmin.feeds.mediastore.navigateToImageGallery
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
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    appState: ClassifiAppState = rememberClassifiAppState(windowSizeClass = windowSizeClass),
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
            onBack = { appState.navController.navigateToHome() },
            onOpenSchoolAdminPanel = { appState.navController.navigateToSchoolAdminPanel() },
            onOpenTeacherAdminPanel = { appState.navController.navigateToTeachersAdminPanel() }
        )

        composeFeedScreen(
            onCloseComposeFeedScreen = { appState.navController.navigateToHome() },
            onTakePhoto = { appState.navController.navigateToTakePhoto() },
            onChooseImage = { appState.navController.navigateToImageGallery() }
        )

        takePhotoScreen(
            onDismissDialog = { appState.navController.navigateToComposeFeed() },
            onClose = { appState.navController.navigateToComposeFeed() },
            onNext = { appState.navController.navigateToComposeFeed() },
            onViewAlbum = {/*TODO()*/ }
        )

        imageGalleryScreen(
            onBackPressed = { appState.navController.navigateToComposeFeed() },
            onDismissDialog = { appState.navController.navigateToComposeFeed() },
            onChooseImage = { appState.navController.navigateToComposeFeed() }
        )

        schoolAdminPanel(
            windowSizeClass = windowSizeClass,
            onBackPressed = { appState.navController.navigateToSettings() }
        )

        teachersAdminPanel(
            windowSizeClass = windowSizeClass,
            onBackPressed = { appState.navController.navigateToSettings() }
        )
    }
}