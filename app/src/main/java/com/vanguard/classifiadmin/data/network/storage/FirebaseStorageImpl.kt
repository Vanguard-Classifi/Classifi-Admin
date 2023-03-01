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
        onResult: (Boolean, String) -> Unit
    ) {
        try {
            val metadata = storageMetadata { contentType = "image/jpg" }
            val avatarRef =
                storageRef.child("${StorageDir.avatars}/$userId/${fileUri.lastPathSegment}")
            val uploadTask = avatarRef.putFile(fileUri, metadata)

            uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount) ->
                onProgress(bytesTransferred, totalByteCount)
            }
                .continueWithTask { task ->
                    if (!task.isSuccessful) throw task.exception!!
                    avatarRef.downloadUrl
                }
                .addOnFailureListener {
                    onResult(false, "")
                }.addOnSuccessListener { downloadUri ->
                    onResult(true, downloadUri.toString())
                }
        } catch (e: Exception) {
            onResult(false, "")
        }
    }

    override suspend fun downloadAvatar(
        fileUri: Uri,
        userId: String,
        onProgress: (Long, Long) -> Unit,
        onResult: (Boolean, Long) -> Unit
    ) {
        try {
            onProgress(0, 0)
            val avatarRef =
                storageRef.child("${StorageDir.avatars}/$userId/${fileUri.lastPathSegment}")
            val downloadTask = avatarRef.getStream { (_, totalBytes), inputStream ->
                var bytesDownloaded: Long = 0
                val buffer = ByteArray(1024)
                var size: Int = inputStream.read(buffer)

                while (size != -1) {
                    bytesDownloaded += size.toLong()
                    onProgress(bytesDownloaded, totalBytes)
                    size = inputStream.read(buffer)
                }
                inputStream.close()
            }
                .addOnSuccessListener { (_, totalBytes) ->
                    onResult(true, totalBytes)
                }
                .addOnFailureListener { _ ->
                    onResult(false, 0)
                }

        } catch (e: Exception) {
            onResult(false, 0L)
        }
    }
}

