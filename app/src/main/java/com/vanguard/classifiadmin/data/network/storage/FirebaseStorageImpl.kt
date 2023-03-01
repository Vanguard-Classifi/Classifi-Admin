package com.vanguard.classifiadmin.data.network.storage

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2

object StorageDir {
    const val avatars = "avatars"
}


@Singleton
class FirebaseStorageImpl @Inject constructor() : FirebaseStorage {
    private val storageRef = Firebase.storage.reference

    override suspend fun uploadAvatar(
        fileUri: Uri,
        userId: String,
        onProgress: (Long, Long) -> Unit,
        onResult: (Boolean) -> Unit
    ) {
        try {
           // val file = Uri.fromFile(File(filePath))
            val metadata = storageMetadata { contentType = "image/jpg" }
            val avatarRef =
                storageRef.child("${StorageDir.avatars}/$userId/${fileUri.lastPathSegment}")
            val uploadTask = avatarRef.putFile(fileUri, metadata)

            uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount) ->
                onProgress(bytesTransferred, totalByteCount)
            }
                .addOnFailureListener {
                    onResult(false)
                }.addOnSuccessListener { _ ->
                    onResult(true)
                }
        } catch (e: Exception) {
            onResult(false)
        }
    }
}

