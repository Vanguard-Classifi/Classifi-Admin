package com.khalidtouch.chatme.admin.school

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import com.google.accompanist.navigation.animation.composable
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.chatme.admin.common.ItemNotAvailable
import com.khalidtouch.chatme.admin.school.addschool.AddSchoolScreen
import com.khalidtouch.chatme.admin.school.addschool.AddSchoolUiState
import com.khalidtouch.chatme.admin.school.addschool.AddSchoolViewModel
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel
import com.khalidtouch.core.designsystem.components.ClassifiFab
import com.khalidtouch.core.designsystem.components.ClassifiSimpleTopAppBar
import com.khalidtouch.core.designsystem.icons.ClassifiIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SchoolRoute(
    windowSizeClass: WindowSizeClass,
    schoolViewModel: SchoolViewModel = hiltViewModel<SchoolViewModel>(),
    addSchoolViewModel: AddSchoolViewModel = hiltViewModel<AddSchoolViewModel>(),
    onBackPressed: () -> Unit,
) {
    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val headerStyle = MaterialTheme.typography.titleMedium

            ClassifiSimpleTopAppBar(
                title = {
                    Box(Modifier.padding(start = 16.dp)) {
                        ProvideTextStyle(headerStyle) {
                            Text(
                                text = stringResource(id = R.string.manage_school)
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
                onClick = schoolViewModel::onShowAddSchoolDialog,
                icon = {
                    Icon(
                        painter = painterResource(ClassifiIcons.Add),
                        contentDescription = stringResource(R.string.add_school)
                    )
                }
            )
        },
        content = {
            SchoolScreen(
                modifier = Modifier.padding(it),
                schoolViewModel = schoolViewModel,
                windowSizeClass = windowSizeClass,
                addSchoolViewModel = addSchoolViewModel,
            )
        }
    )
}


@Composable
private fun SchoolScreen(
    modifier: Modifier = Modifier,
    schoolViewModel: SchoolViewModel,
    addSchoolViewModel: AddSchoolViewModel,
    windowSizeClass: WindowSizeClass,
) {
    val uiState by schoolViewModel.uiState.collectAsStateWithLifecycle()
    val state = rememberLazyListState()
    val configuration = LocalConfiguration.current

    when (uiState) {
        is SchoolScreenUiState.Loading -> Unit
        is SchoolScreenUiState.Success -> {
            LazyColumn(
                state = state,
                modifier = modifier
                    .fillMaxSize()
                    .testTag("admin:school")
            ) {

            }

            ItemNotAvailable(
                headerText = stringResource(id = R.string.no_school_added),
                labelText = stringResource(id = R.string.click_plus_to_add)
            )

            if ((uiState as SchoolScreenUiState.Success).data.shouldShowAddSchoolDialog) {
                AddSchoolScreen(
                    windowSizeClass = windowSizeClass,
                    schoolViewModel = schoolViewModel,
                    addSchoolViewModel = addSchoolViewModel,
                )
            }
        }
    }


    AnimatedVisibility(
        visible = uiState is SchoolScreenUiState.Loading,
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


const val schoolNavigationRoute = "school_navigation_route"

fun NavController.navigateToSchoolScreen() {
    this.navigate(schoolNavigationRoute) {
        launchSingleTop = true
        popUpTo(schoolNavigationRoute)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.schoolScreen(
    windowSizeClass: WindowSizeClass,
    onBackPressed: () -> Unit,
) {
    composable(
        route = schoolNavigationRoute,
    ) {
        SchoolRoute(
            onBackPressed = onBackPressed,
            windowSizeClass = windowSizeClass,
        )
    }
}