package com.vanguard.classifiadmin.data.network.storage

import android.net.Uri

interface FirebaseStorage {
    suspend fun uploadAvatar(
        fileUri: Uri,
        userId: String,
        onProgress: (Long, Long) -> Unit,
        onResult: (Boolean) -> Unit
    )
}