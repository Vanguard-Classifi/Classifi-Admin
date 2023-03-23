@file:Suppress("DEPRECATION")

package com.vanguard.classifiadmin.data.file

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.vanguard.classifiadmin.domain.helpers.CacheManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class CacheManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : CacheManager {
    private val TAG = "CacheManagerImpl"

    override suspend fun saveImageFile(bitmap: Bitmap, filename: String): File? {
        var cachePath: File? = null
        try {
            if (filename.isNotBlank()) {
                cachePath = File(context.applicationContext.cacheDir, IMAGE_CHILD_DIR)
                cachePath.mkdirs()
                val stream = withContext(Dispatchers.IO) {
                    FileOutputStream("$cachePath/$filename$IMAGE_EXTENSION")
                }
                bitmap.compress(Bitmap.CompressFormat.PNG, IMAGE_COMPRESS_QUALITY, stream)
                withContext(Dispatchers.IO) {
                    stream.close()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return cachePath
    }

    override suspend fun saveImageFileAndReturnUri(bitmap: Bitmap): Uri? {
        return saveImageFileAndReturnUri(bitmap, IMAGE_TEMP_FILE_NAME)
    }

    override suspend fun saveImageFileAndReturnUri(bitmap: Bitmap, filename: String): Uri? {
        val file = saveImageFile(bitmap, filename) ?: return null
        return getImageUri(file, filename)
    }

    override suspend fun getUriFromFilename(filename: String): Uri? {
        if (filename.isNotBlank()) {
            val path = File(context.applicationContext.cacheDir, IMAGE_CHILD_DIR)
            val file = File(path, "$filename$IMAGE_EXTENSION")
            return FileProvider.getUriForFile(
                context.applicationContext,
                "${context.applicationContext.packageName}.provider", file
            )
        }
        return null
    }

    override suspend fun getContentType(uri: Uri): String? {
        return context.applicationContext.contentResolver.getType(uri)
    }

    override suspend fun getBitmapFromUri(uri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        return try {
            bitmap = MediaStore.Images.Media.getBitmap(
                context.applicationContext.contentResolver,
                uri
            )
            bitmap
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }


    private fun getImageUri(fileDir: File, filename: String): Uri? {
        if (filename.isNotBlank()) {
            val file = File(fileDir, "$filename$IMAGE_EXTENSION")
            return FileProvider.getUriForFile(
                context.applicationContext,
                "${context.applicationContext.packageName}.provider", file
            )
        }
        return null
    }


    companion object {
        private const val IMAGE_CHILD_DIR = "images"
        private val IMAGE_TEMP_FILE_NAME = "img/${System.currentTimeMillis()}"
        private const val IMAGE_EXTENSION = ".png"
        private const val IMAGE_COMPRESS_QUALITY = 100
    }

}