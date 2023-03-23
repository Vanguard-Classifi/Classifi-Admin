package com.vanguard.classifiadmin.domain.helpers

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

interface CacheManager {
    suspend fun saveImageFile(bitmap: Bitmap, filename: String): File?

    suspend fun saveImageFileAndReturnUri(bitmap: Bitmap): Uri?

    suspend fun saveImageFileAndReturnUri(bitmap: Bitmap, filename: String): Uri?

    suspend fun getUriFromFilename(filename: String): Uri?

    suspend fun getContentType(uri: Uri): String?

    suspend fun getBitmapFromUri(uri: Uri): Bitmap?
}