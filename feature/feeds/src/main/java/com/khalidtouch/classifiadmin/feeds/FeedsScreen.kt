package com.khalidtouch.classifiadmin.feeds

import android.util.Log
import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel
import com.khalidtouch.core.designsystem.components.ClassifiFab
import com.khalidtouch.core.designsystem.components.ClassifiIconButton
import com.khalidtouch.core.designsystem.components.ClassifiIconButtonDefaults
import com.khalidtouch.core.designsystem.icons.ClassifiIcons


@Composable
internal fun FeedsRoute(
    onComposeFeed: () -> Unit,
    feedsViewModel: FeedsViewModel = hiltViewModel<FeedsViewModel>()
) {
    val onboardingUiState by feedsViewModel.onboardingUiState.collectAsStateWithLifecycle()
    val feedState by feedsViewModel.feedState.collectAsStateWithLifecycle()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ClassifiFab(
                onClick = onComposeFeed,
                icon = {
                    Icon(
                        painter = painterResource(ClassifiIcons.Compose),
                        contentDescription = stringResource(R.string.compose)
                    )
                }
            )
        },
        content = { padding ->
            FeedsScreen(
                modifier = Modifier.padding(padding),
                isSyncing = false,
                onboardingUiState = onboardingUiState,
                feedUiState = feedState,
                onStartComment = { /*TODO*/ },
                onLikeCheckedChanged = { feedId, state ->
                    feedsViewModel.toggleLikeStateOfFeed(
                        feedId,
                        state
                    )
                },
                onSharePost = { /*TODO*/ },
                onViewDetails = { /*TODO*/ },
                onViewAuthor = {/*TODO -> */ }
            )
        }
    )
}


@Composable
internal fun FeedsScreen(
    modifier: Modifier = Modifier,
    isSyncing: Boolean,
    onboardingUiState: OnboardingUiState,
    feedUiState: NewsFeedUiState,
    onStartComment: () -> Unit,
    onLikeCheckedChanged: (Long, Boolean) -> Unit,
    onSharePost: () -> Unit,
    onViewDetails: () -> Unit,
    onViewAuthor: () -> Unit,
) {
    val isOnboardingLoading = onboardingUiState is OnboardingUiState.Loading
    val isFeedLoading = feedUiState is NewsFeedUiState.Loading

    ReportDrawnWhen { !isFeedLoading || !isOnboardingLoading }
    val state = rememberLazyListState()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = state,
        modifier = modifier
            .fillMaxSize()
            .testTag("feeds:feed")
    ) {
        newsFeed(
            feedState = feedUiState,
            onStartComment = onStartComment,
            onLikeCheckedChanged = onLikeCheckedChanged,
            onSharePost = onSharePost,
            onViewDetails = onViewDetails,
            onViewAuthor = onViewAuthor
        )

        item {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }

    /*TODO -> show onboarding screen */

    AnimatedVisibility(
        visible = isFeedLoading || isOnboardingLoading,
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