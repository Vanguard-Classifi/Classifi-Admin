package com.vanguard.classifiadmin.domain.services

import android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.IBinder
import com.vanguard.classifiadmin.data.repository.MainRepository
import com.vanguard.classifiadmin.domain.services.DownloadServiceActions.ACTION_DOWNLOAD
import com.vanguard.classifiadmin.domain.services.DownloadServiceActions.ACTION_DOWNLOAD_ERROR
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

object DownloadServiceExtras {
    const val downloadFileUri: String = "downloadedFileUriDownload"
    const val currentUserId: String = "currentUserIdDownload"
}

object DownloadServiceActions {
    const val ACTION_DOWNLOAD = "action_download"
    const val ACTION_DOWNLOAD_ERROR = "action_download_error"
    const val ACTION_DOWNLOAD_COMPLETED = "action_download_completed"
}

@AndroidEntryPoint
class AvatarDownloadService : BaseService() {
    @Inject
    lateinit var repository: MainRepository


    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {
            if (intent?.action == ACTION_DOWNLOAD){
                val fileUri = intent.getParcelableExtra<Uri>(DownloadServiceExtras.downloadFileUri)!!
                val userId = intent.getStringExtra(UploadServiceExtras.currentUserId)!!

                downloadAvatar(fileUri, userId)
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private suspend fun downloadAvatar(avatar: Uri, userId: String) {


    }


    companion object {
        val intentFilter: IntentFilter
            get() {
                val filter = IntentFilter()
                filter.addAction(ACTION_DOWNLOAD)
                filter.addAction(ACTION_DOWNLOAD_ERROR)
                filter.addAction(ACTION_DOWNLOAD_COMPLETE)
                return filter
            }
    }
}