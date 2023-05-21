package com.khalidtouch.classifiadmin.feeds

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import com.khalidtouch.core.designsystem.ui.NewsFeedCard


fun LazyListScope.newsFeed(
    feedState: NewsFeedUiState,
    onStartComment: () -> Unit,
    onLikeCheckedChanged: (Long, Boolean) -> Unit,
    onSharePost: () -> Unit,
    onViewDetails: () -> Unit,
    onViewAuthor: () -> Unit,
) {
    when (feedState) {
        NewsFeedUiState.Loading -> Unit
        is NewsFeedUiState.Success -> {
            items(feedState.feed, key = { it.feedId }) { userNewsFeed ->
                NewsFeedCard(
                    newsFeed = userNewsFeed,
                    isLiked = userNewsFeed.isLiked,
                    onToggleLike = onLikeCheckedChanged,
                    onClick = { onViewDetails() },
                    onStartComment = { onStartComment() },
                    onViewAuthor = { onViewAuthor() },
                    onSharePost = { onSharePost() })
            }
        }
    }
}