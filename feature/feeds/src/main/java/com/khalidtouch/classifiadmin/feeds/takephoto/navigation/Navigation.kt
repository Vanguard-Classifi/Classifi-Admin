package com.khalidtouch.classifiadmin.feeds.takephoto.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.khalidtouch.classifiadmin.feeds.takephoto.TakePhotoRoute

const val takePhotoNavigationRoute = "takePhotoNavigationRoute"

fun NavController.navigateToTakePhoto(navOptions: NavOptions? = null) {
    this.navigate(takePhotoNavigationRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.takePhotoScreen(
    onDismissDialog: () -> Unit,
    onClose: () -> Unit,
    onNext: () -> Unit,
    onViewAlbum: () -> Unit,
) {
    composable(
        route = takePhotoNavigationRoute,
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
        TakePhotoRoute(
            onDismissDialog = onDismissDialog,
            onClose = onClose,
            onNext = onNext,
            onViewAlbum = onViewAlbum
        )
    }
}