package com.khalidtouch.classifiadmin.feeds.mediastore

import android.net.Uri

sealed interface GalleryUiState {
    object Loading : GalleryUiState
    data class Success(val data: GalleryData) : GalleryUiState
}

data class GalleryData(
    val mediaSelection: MediaSelection,
)

data class MediaData(
    val uri: Uri,
    val filename: String,
    val mimeType: String,
)

data class MediaSelection(
    val collection: String,
    val selectedMedias: HashMap<String, Boolean>,
    var chosenImage: MediaData? = null,
    val loadedImages: List<MediaData>,
    val canBeAdded: Boolean,
)