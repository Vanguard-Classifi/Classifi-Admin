package com.khalidtouch.classifiadmin.feeds.mediastore

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi

const val imageGalleryNavigationRoute = "image_gallery_navigation_route"

fun NavController.navigateToImageGallery(navOptions: NavOptions? = null) {
    this.navigate(imageGalleryNavigationRoute, navOptions)
}


@OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
fun NavGraphBuilder.imageGalleryScreen(
    onBackPressed: () -> Unit,
    onDismissDialog: () -> Unit,
    onChooseImage: () -> Unit,
) {
    composable(route = imageGalleryNavigationRoute,
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
        }) {
        GalleryRoute(
            onBackPressed = onBackPressed,
            onDismissDialog = onDismissDialog,
            onChooseImage = onChooseImage,
        )
    }
}