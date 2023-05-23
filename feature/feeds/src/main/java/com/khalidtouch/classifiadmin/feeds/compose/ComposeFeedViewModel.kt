package com.khalidtouch.classifiadmin.feeds.compose

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalidtouch.chatme.datastore.ClassifiPreferencesDataSource
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
    private val prefDataSource: ClassifiPreferencesDataSource,
) : ViewModel() {
    val TAG = "ComposeFeed"
    private val _feedTitle = MutableStateFlow<String>("")
    val feedTitle: StateFlow<String> = _feedTitle

    private val _feedContent = MutableStateFlow<String>("")
    val feedContent: StateFlow<String> = _feedContent

    private val _currentComposeFeedBottomSheetSelection =
        MutableStateFlow<ComposeFeedBottomSheetSelection>(ComposeFeedBottomSheetSelection.None)
    val currentComposeFeedBottomSheetSelection: StateFlow<ComposeFeedBottomSheetSelection> =
        _currentComposeFeedBottomSheetSelection


    val isFeedPostable: StateFlow<Boolean> =
        combine(
            _feedTitle,
            _feedContent
        ) { title, content ->
            title.isNotBlank() || content.isNotBlank()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = false
        )

    val isBottomSheetShown: StateFlow<Boolean> =
        _currentComposeFeedBottomSheetSelection.map { it !is ComposeFeedBottomSheetSelection.None }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(2_000),
                initialValue = false
            )

    private val emptyImageUri: Uri = Uri.parse("file://dev/null")
    private val _imageUri = MutableStateFlow<Uri>(emptyImageUri)
    val imageUri: StateFlow<Uri> = _imageUri

    val hasNoSavedImage: StateFlow<Boolean> =
        combine(
            _imageUri,
            flowOf(emptyImageUri)
        ) { image, emptyImage ->
            image == emptyImage
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = false,
        )

    fun updatePhotoUri() = viewModelScope.launch {
        try {
            prefDataSource.userData.collect {
                val lastMessage = it.feedData.messages.last()
                _imageUri.value = Uri.parse(lastMessage.uri)
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: NoSuchElementException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

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
}

sealed interface ComposeFeedBottomSheetSelection {

    object None : ComposeFeedBottomSheetSelection
    object Attachments : ComposeFeedBottomSheetSelection
    object Classes : ComposeFeedBottomSheetSelection
}