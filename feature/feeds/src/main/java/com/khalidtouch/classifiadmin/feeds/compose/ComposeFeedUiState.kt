package com.khalidtouch.classifiadmin.feeds.compose

import android.net.Uri
import com.khalidtouch.classifiadmin.model.FeedMessage

sealed interface ComposeFeedUiState {
    object Loading : ComposeFeedUiState
    data class Success(val data: ComposeFeedData) : ComposeFeedUiState
}

data class ComposeFeedData(
    val userId: Long,
    val username: String,
    val profileImage: Uri,
    val feedTextEntry: FeedTextEntry,
    val bottomSheetState: ComposeFeedBottomSheetSelection,
    val isBottomSheetShown: Boolean,
    val isFeedPostable: Boolean,
    val mediaMessages: List<FeedMessage>,
    val hasNoSavedMedia: Boolean,
    val request: ComposeFeedRequest,
)

data class FeedTextEntry(
    val title: String,
    val content: String,
)


data class ComposeFeedRequest(
    val postRequest: Boolean,
    val abortRequest: Boolean
)