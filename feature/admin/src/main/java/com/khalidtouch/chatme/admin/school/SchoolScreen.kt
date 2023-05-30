package com.khalidtouch.chatme.admin.school

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import com.google.accompanist.navigation.animation.composable
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.core.designsystem.components.ClassifiBackground
import com.khalidtouch.core.designsystem.components.ClassifiFab
import com.khalidtouch.core.designsystem.components.ClassifiGradientBackground
import com.khalidtouch.core.designsystem.components.ClassifiSimpleTopAppBar
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.khalidtouch.core.designsystem.theme.LocalGradientColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SchoolRoute(
    schoolViewModel: SchoolViewModel = hiltViewModel<SchoolViewModel>(),
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
            )
        }
    )
}


@Composable
private fun SchoolScreen(
    modifier: Modifier = Modifier,
    schoolViewModel: SchoolViewModel,
) {
    val uiState by schoolViewModel.uiState.collectAsStateWithLifecycle()
    val state = rememberLazyListState()
    LazyColumn(
        state = state,
        modifier = modifier
            .fillMaxSize()
            .testTag("admin:school")
    ) {

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
    onBackPressed: () -> Unit,
) {
    composable(
        route = schoolNavigationRoute,
    ) {
        SchoolRoute(
            onBackPressed = onBackPressed,
        )
    }
}