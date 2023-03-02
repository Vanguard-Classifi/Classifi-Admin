package com.vanguard.classifiadmin.domain.downloader

import android.app.DownloadManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AndroidDownloader @Inject constructor(
    @ApplicationContext private val context: Context,
) : Downloader {
    val TAG = "AndroidDownloader"
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/*")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setTitle("${System.currentTimeMillis().toString()}.jpg")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "avatars")
        return downloadManager.enqueue(request)
    }

    override fun downloadAvatar(url: String) {
        try {
            val fileUri = Uri.parse(url)
            val stream = context.contentResolver.openInputStream( Uri.parse("https://firebasestorage.googleapis.com/v0/b/classifi-3ee05.appspot.com/o/avatars%2F08iPheZ13yMto3NPmun0NDlq5kK2%2Fimage%3A111121?alt=media&token=6432eafe-7685-4409-929f-b8e391802a7f"))
            //transform to bitmap
            val bitmap = BitmapFactory.decodeStream(stream)

            val wrapper = ContextWrapper(context.applicationContext)
            val directory = wrapper.getDir("avatars", Context.MODE_PRIVATE)
            val file = File(directory, "${System.currentTimeMillis().toString()}.jpg")
            if (!file.exists()) {
                //save file to storage
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            }
            stream?.close()
            Log.e(TAG, "downloadAvatar: Profile Image has been saved")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}