package com.khalidtouch.chatme.admin.teachers.addteacher

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.google.accompanist.navigation.animation.composable

@Composable
fun InputTeacherSuccessfulScreen() {
    LazyColumn {
        tickItem()
    }
}

private fun LazyListScope.tickItem() {
    item {
        Box {
            Icon(
                painter = painterResource(id = ClassifiIcons.Success),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .height(300.dp)
                    .width(300.dp)
            )
        }

        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(id = R.string.successfully_added),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}


const val inputTeacherSuccessfulScreenNavigationRoute =
    "input_teacher_successful_screen_navigation_route"

fun NavController.navigateToInputTeacherSuccessfulScreen(
    addTeacherViewModel: AddTeacherViewModel,
    navOptions: NavOptions? = null,
) {
    this.navigate(inputTeacherSuccessfulScreenNavigationRoute, navOptions)
    addTeacherViewModel.onNavigate(AddTeacherPage.SUCCESS)
}


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.inputTeacherSuccessfulScreen() {
    composable(
        route = inputTeacherSuccessfulScreenNavigationRoute,
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
        InputTeacherSuccessfulScreen()
    }
}