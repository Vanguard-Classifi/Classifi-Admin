package com.khalidtouch.classifiadmin.feeds

import com.khalidtouch.chatme.domain.models.UserNewsFeed

sealed interface NewsFeedUiState {
    object Loading : NewsFeedUiState

    data class Success(
        val feed: List<UserNewsFeed>
    ) : NewsFeedUiState
}