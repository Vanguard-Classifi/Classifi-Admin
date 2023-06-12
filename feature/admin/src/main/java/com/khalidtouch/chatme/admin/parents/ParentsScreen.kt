package com.khalidtouch.chatme.admin.parents

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import com.google.accompanist.navigation.animation.composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.chatme.admin.common.ItemNotAvailable
import com.khalidtouch.chatme.admin.parents.addparent.AddParentScreen
import com.khalidtouch.chatme.admin.parents.addparent.AddParentViewModel
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import com.khalidtouch.core.common.extensions.ifNullOrBlank
import com.khalidtouch.core.common.extensions.isEqualToDefaultImageUrl
import com.khalidtouch.core.common.extensions.orDefaultImageUrl
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel
import com.khalidtouch.core.designsystem.components.ClassifiBackground
import com.khalidtouch.core.designsystem.components.ClassifiFab
import com.khalidtouch.core.designsystem.components.ClassifiGradientBackground
import com.khalidtouch.core.designsystem.components.ClassifiSimpleTopAppBar
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.khalidtouch.core.designsystem.theme.LocalGradientColors
import com.khalidtouch.core.designsystem.ui.ClassifiUserThumbnail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ParentRoute(
    onBackPressed: () -> Unit,
    parentScreenViewModel: ParentScreenViewModel,
    addParentViewModel: AddParentViewModel,
    windowSizeClass: WindowSizeClass,
) {
    ClassifiBackground {
        ClassifiGradientBackground(
            gradientColors = LocalGradientColors.current
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    val headerStyle = MaterialTheme.typography.titleMedium

                    ClassifiSimpleTopAppBar(
                        title = {
                            Box(Modifier.padding(start = 16.dp)) {
                                ProvideTextStyle(headerStyle) {
                                    Text(
                                        text = stringResource(id = R.string.manage_parents)
                                    )
                                }
                            }
                        },
                        navIcon = {
                            Box(modifier = Modifier) {
                                IconButton(
                                    onClick = onBackPressed,
                                    enabled = true,
                                ) {
                                    Icon(
                                        painter = painterResource(id = ClassifiIcons.Back),
                                        contentDescription = stringResource(id = R.string.go_back)
                                    )
                                }
                            }
                        }
                    )
                },
                floatingActionButton = {
                    ClassifiFab(
                        onClick = { parentScreenViewModel.updateAddParentDialogState(true) },
                        icon = {
                            Icon(
                                painter = painterResource(ClassifiIcons.Add),
                                contentDescription = stringResource(R.string.add_parent)
                            )
                        }
                    )
                },
                content = {
                    ParentsScreen(
                        modifier = Modifier.padding(it),
                        parentScreenViewModel = parentScreenViewModel,
                        addParentViewModel = addParentViewModel,
                        windowSizeClass = windowSizeClass
                    )
                }
            )
        }
    }
}


@Composable
private fun ParentsScreen(
    modifier: Modifier = Modifier,
    parentScreenViewModel: ParentScreenViewModel,
    addParentViewModel: AddParentViewModel,
    windowSizeClass: WindowSizeClass,
) {
    val state = rememberLazyListState()
    val uiState by parentScreenViewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is ParentScreenUiState.Loading -> Unit
        is ParentScreenUiState.Success -> {
            val listOfParents =
                (uiState as ParentScreenUiState.Success).data.listOfParents

            LazyColumn(
                state = state,
                modifier = modifier
                    .fillMaxSize()
                    .testTag("admin:parent"),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                /*TODO() -> populate parents using paging*/
            }

            ItemNotAvailable(
                headerText = stringResource(id = R.string.no_parent_added),
                labelText = stringResource(id = R.string.click_plus_to_add)
            )
        }
    }

    when(uiState) {
        is ParentScreenUiState.Loading -> Unit
        is ParentScreenUiState.Success -> {
            if((uiState as ParentScreenUiState.Success).data.shouldShowAddParentDialog) {
                AddParentScreen(
                    addParentViewModel = addParentViewModel,
                    parentScreenViewModel = parentScreenViewModel,
                    windowSizeClass = windowSizeClass,
                )
            }
        }
    }


    AnimatedVisibility(
        visible = uiState is ParentScreenUiState.Success,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
        ) + fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            ClassifiLoadingWheel()
        }
    }
}

@Composable
private fun ParentItem(
    parent: ClassifiUser,
    selected: Boolean,
    onClick: (ClassifiUser) -> Unit,
    onLongPress: (ClassifiUser) -> Unit,
) {
    ClassifiUserThumbnail(
        username = parent.account?.username.ifNullOrBlank(stringResource(id = R.string.name_not_specified)),
        email = parent.account?.email.ifNullOrBlank(stringResource(id = R.string.email_not_added)),
        profileImage = parent.profile?.profileImage.orDefaultImageUrl(),
        bio = parent.profile?.bio.ifNullOrBlank(stringResource(id = R.string.empty_bio)),
        selected = selected,
        onClick = { onClick(parent) },
        onLongPress = { onLongPress(parent) },
        profileImageIsDefault = parent.profile?.profileImage?.isEqualToDefaultImageUrl() ?: true
    )
}

const val parentsScreenNavigationRoute = "parents_screen_navigation_route"

fun NavController.navigateToParentsScreen() {
    this.navigate(parentsScreenNavigationRoute) {
        launchSingleTop = true
        popUpTo(parentsScreenNavigationRoute)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.parentsScreen(
    windowSizeClass: WindowSizeClass,
    onBackPressed: () -> Unit,
    parentScreenViewModel: ParentScreenViewModel,
    addParentViewModel: AddParentViewModel,
) {
    composable(
        route = parentsScreenNavigationRoute,
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
        ParentRoute(
            onBackPressed = onBackPressed,
            windowSizeClass = windowSizeClass,
            parentScreenViewModel = parentScreenViewModel,
            addParentViewModel = addParentViewModel,
        )
    }
}