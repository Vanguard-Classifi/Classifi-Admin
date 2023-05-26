package com.khalidtouch.classifiadmin.feeds.mediastore

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.classifiadmin.model.FeedMessage
import com.khalidtouch.classifiadmin.model.MessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {
    val TAG = "GalleryVM"
    private val _mediaCollection = MutableStateFlow<String>("")
    private val _selectedMediaData =
        MutableStateFlow<HashMap<String, Boolean>>(hashMapOf<String, Boolean>())
    private val _allImageMediaData = MutableStateFlow<List<MediaData>>(listOf())
    val selectedMedia: StateFlow<HashMap<String, Boolean>> = _selectedMediaData
    private val _chosenImage = MutableStateFlow<MediaData?>(null)


    val uiState: StateFlow<GalleryUiState> = combine(
        _mediaCollection,
        _selectedMediaData,
        _allImageMediaData,
        _chosenImage,
    ) { collection, mediaData, images, chosenImage ->
        GalleryUiState.Success(
            data = GalleryData(
                mediaSelection = MediaSelection(
                    collection = collection,
                    selectedMedias = mediaData,
                    canBeAdded = mediaData.isNotEmpty(),
                    loadedImages = images,
                    chosenImage = chosenImage
                )
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = GalleryUiState.Loading,
    )

    fun onToggleSelection(selected: Boolean, mediaData: String) {
        Log.e(TAG, "onToggleSelection: has been called")
        _selectedMediaData.value[mediaData] = true
        Log.e(TAG, "onToggleSelection: size of map is ${_selectedMediaData.value.size}")
        when (selected) {
            false -> {
                _selectedMediaData.value.remove(mediaData)
                Log.e(TAG, "onToggleSelection: remove has been called")
            }

            true -> {
                _selectedMediaData.value[mediaData] = true
                Log.e(TAG, "onToggleSelection: adding has been called")
            }
        }
    }

    fun onChooseImage(mediaData: MediaData) = viewModelScope.launch {
        userDataRepository.userData.map {
            val id = it.feedData.messages.size.toLong()
            userDataRepository.enqueueMediaMessage(
                FeedMessage(
                    messageId = id,
                    uri = mediaData.uri.toString(),
                    feedType = MessageType.ImageMessage,
                )
            )
            Log.e(TAG, "onChooseImage: enqueue image")
        }
    }

    fun onSetMediaCollection(collection: String) {
        _mediaCollection.value = collection
    }

    fun loadMediaFromStore(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        _allImageMediaData.value = context.getImageUris()
    }
}


fun Context.getImageUris(): List<MediaData> {
    val imageUris = mutableListOf<Uri>()
    val columns = arrayOf(MediaStore.Images.Media._ID)
    val orderBy = MediaStore.Images.Media.DATE_MODIFIED

    this.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        columns, null,
        null, "$orderBy DESC"
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            imageUris.add(
                ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
            )
        }
    }

    return imageUris.map {
        MediaData(
            uri = it,
            filename = "",
            mimeType = "image/*",
        )
    }
}