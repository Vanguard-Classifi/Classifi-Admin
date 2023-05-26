package com.khalidtouch.classifiadmin.feeds.compose

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalidtouch.chatme.datastore.ClassifiPreferencesDataSource
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.classifiadmin.model.FeedMessage
import com.khalidtouch.classifiadmin.model.MessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ComposeFeedViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {
    val TAG = "ComposeFeed"
    private val _feedTitle = MutableStateFlow<String>("")
    private val _feedContent = MutableStateFlow<String>("")
    private val _feedTextEntry: StateFlow<FeedTextEntry> = combine(
        _feedTitle,
        _feedContent,
    ) { title, content ->
        FeedTextEntry(
            title = title,
            content = content,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = FeedTextEntry("", "")
    )

    private val _currentComposeFeedBottomSheetSelection =
        MutableStateFlow<ComposeFeedBottomSheetSelection>(ComposeFeedBottomSheetSelection.None)
    private val _hasPostRequest = MutableStateFlow<Boolean>(false)
    private val _hasAbortRequest = MutableStateFlow<Boolean>(false)
    private val _request: StateFlow<ComposeFeedRequest> =
        combine(
            _hasPostRequest,
            _hasAbortRequest
        ) { hasPostRequest, hasAbortRequest ->
            ComposeFeedRequest(
                postRequest = hasPostRequest,
                abortRequest = hasAbortRequest
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ComposeFeedRequest(
                postRequest = false,
                abortRequest = false
            )
        )

    private val emptyImageUri: Uri = Uri.parse("file://dev/null")

    val uiState: StateFlow<ComposeFeedUiState> =
        combine(
            userDataRepository.userData,
            _currentComposeFeedBottomSheetSelection,
            _feedTextEntry,
            _request,
        ) { data, bottomSheetState, textEntry, request ->
            val mediaMessages  = data.feedData.messages
                .filterNot { it.feedType == MessageType.TextMessage || it.feedType == MessageType.Unknown }

            ComposeFeedUiState.Success(
                data = ComposeFeedData(
                    userId = data.userId,
                    username = data.userName,
                    profileImage = Uri.parse(data.profileImage),
                    feedTextEntry = textEntry,
                    bottomSheetState = bottomSheetState,
                    request = request,
                    isBottomSheetShown = bottomSheetState !is ComposeFeedBottomSheetSelection.None,
                    isFeedPostable = textEntry.title.isNotBlank() || textEntry.content.isNotBlank() ||
                            mediaMessages.isNotEmpty(),
                    mediaMessages = mediaMessages,
                    hasNoSavedMedia = mediaMessages.isEmpty(),
                )
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(2000),
                initialValue = ComposeFeedUiState.Loading
            )


    fun onTitleValueChange(newTitle: String) {
        _feedTitle.value = newTitle
    }

    fun onContentValueChange(newContent: String) {
        _feedContent.value = newContent
    }

    fun onComposeFeedBottomSheetSelectionChange(selection: ComposeFeedBottomSheetSelection) {
        _currentComposeFeedBottomSheetSelection.value = selection
    }

    fun clearBottomSheetSelection() {
        _currentComposeFeedBottomSheetSelection.value = ComposeFeedBottomSheetSelection.None
    }

    fun enqueueMediaMessage() {
        viewModelScope.launch {
            userDataRepository.userData.map {
                val id = it.feedData.messages.size.toLong()

                userDataRepository.enqueueMediaMessage(
                    FeedMessage(
                        messageId = id,
                        uri = "https://www.petsworld.in/blog/wp-content/uploads/2014/09/funny-cat.jpg",
                        feedType = MessageType.ImageMessage,
                    )
                )
            }
        }
    }

    fun onDeleteMediaMessage(message: FeedMessage) {
        viewModelScope.launch {
            userDataRepository.deleteMessageById(message.messageId)
            Log.e(TAG, "onDeleteMediaMessage: called of messageId ${message.messageId}")
        }
    }
}

sealed interface ComposeFeedBottomSheetSelection {
    object None : ComposeFeedBottomSheetSelection
    object Attachments : ComposeFeedBottomSheetSelection
    object Classes : ComposeFeedBottomSheetSelection
}