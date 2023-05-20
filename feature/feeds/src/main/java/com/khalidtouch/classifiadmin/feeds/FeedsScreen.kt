package com.khalidtouch.classifiadmin.feeds

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel


@Composable
internal fun FeedsRoute(
    modifier: Modifier = Modifier,
    feedsViewModel: FeedsViewModel = hiltViewModel<FeedsViewModel>()
) {

}


@Composable
internal fun FeedsScreen(
    modifier: Modifier = Modifier,
    isSyncing: Boolean,
    onboardingUiState: OnboardingUiState,
    feedUiState: NewsFeedUiState,
    onStartComment: () -> Unit,
    onLikeCheckedChanged: (Boolean) -> Unit,
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

    AnimatedVisibility(
        visible = isSyncing || isFeedLoading || isOnboardingLoading,
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
                .padding(top = 8.dp)
        ) {
            ClassifiLoadingWheel()
        }
    }
}