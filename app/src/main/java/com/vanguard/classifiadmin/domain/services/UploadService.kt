package com.vanguard.classifiadmin.domain.services

import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.IBinder
import com.vanguard.classifiadmin.MainActivity
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.repository.MainRepository
import com.vanguard.classifiadmin.domain.services.UploadServiceActions.ACTION_COMPLETED
import com.vanguard.classifiadmin.domain.services.UploadServiceActions.ACTION_ERROR
import com.vanguard.classifiadmin.domain.services.UploadServiceActions.ACTION_UPLOAD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

object UploadServiceExtras {
    const val uploadedFileUri: String = "uploaded_file_uri"
    const val currentUserId: String = "currentUserId"
}

object UploadServiceActions {
    const val ACTION_UPLOAD = "action_upload"
    const val ACTION_ERROR = "action_error"
    const val ACTION_COMPLETED = "action_completed"
}


@AndroidEntryPoint
class UploadService : BaseUploadService() {


    @Inject
    lateinit var repository: MainRepository

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {
            if (intent?.action == ACTION_UPLOAD) {
                val fileUri = intent.getParcelableExtra<Uri>(UploadServiceExtras.uploadedFileUri)!!
                val userId = intent.getStringExtra(UploadServiceExtras.currentUserId)!!
                contentResolver.takePersistableUriPermission(
                    fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                uploadAvatar(fileUri, userId)
            }
        }


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private suspend fun uploadAvatar(avatar: Uri, userId: String) {
        //start task
        progressNotification(getString(R.string.uploading_avatar), 0, 0)

        //do your thing
        repository.uploadAvatar(
            fileUri = avatar,
            userId = userId,
            onProgress = { current, total ->
                progressNotification(getString(R.string.uploading), current, total)
            },
            onResult = { success ->
                if (success) {
                    showUploadFinishedNotification(avatar)
                }
            },
        )
    }


    private fun showUploadFinishedNotification(fileUri: Uri) {
        dismissProgressNotification()
        val intent = Intent(this, MainActivity::class.java)
            .putExtra(UploadServiceExtras.uploadedFileUri, fileUri)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        completedNotification(getString(R.string.upload_success), intent, true)
    }

    companion object {
        val intentFilter: IntentFilter
            get() {
                val filter = IntentFilter()
                filter.addAction(ACTION_UPLOAD)
                filter.addAction(ACTION_COMPLETED)
                filter.addAction(ACTION_ERROR)
                return filter
            }
    }
}
