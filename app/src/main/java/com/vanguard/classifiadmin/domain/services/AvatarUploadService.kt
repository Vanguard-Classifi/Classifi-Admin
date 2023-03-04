package com.vanguard.classifiadmin.domain.services

import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.IBinder
import android.util.Log
import com.vanguard.classifiadmin.data.network.models.UserNetworkModel
import com.vanguard.classifiadmin.data.repository.MainRepository
import com.vanguard.classifiadmin.domain.downloader.Downloader
import com.vanguard.classifiadmin.domain.helpers.today
import com.vanguard.classifiadmin.domain.services.UploadServiceActions.ACTION_COMPLETED
import com.vanguard.classifiadmin.domain.services.UploadServiceActions.ACTION_ERROR
import com.vanguard.classifiadmin.domain.services.UploadServiceActions.ACTION_UPLOAD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
class AvatarUploadService : BaseService() {
    val TAG = "AvatarUploadService"

    @Inject
    lateinit var repository: MainRepository

    @Inject
    lateinit var downloader: Downloader

    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private var currentUser: UserNetworkModel? = null
    private var downloadUrl: String = ""

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {
            if (intent?.action == ACTION_UPLOAD) {
                try {
                    val fileUri =
                        intent.getParcelableExtra<Uri>(UploadServiceExtras.uploadedFileUri)!!
                    val userId = intent.getStringExtra(UploadServiceExtras.currentUserId)!!
                    contentResolver.takePersistableUriPermission(
                        fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )

                    uploadAvatar(fileUri, userId)
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private suspend fun uploadAvatar(avatar: Uri, userId: String) {
        repository.uploadAvatar(
            fileUri = avatar,
            userId = userId,
            onProgress = { _, _ ->
            },
            onResult = { _, url ->
                scope.launch {
                    downloadUrl = url
                    Log.e(TAG, "uploadAvatar: the download url is $url")
                    updateProfileImage(userId, url)
                    downloader.downloadFile(url)
                }
            },
        )

    }

    private suspend fun updateProfileImage(userId: String, url: String) {
        Log.e(TAG, "updateProfileImage: fetching current user")
        repository.getUserByIdNetwork(userId) { userResource ->
            //fetch the user by id
            currentUser = userResource.data
        }
        delay(2000)
        //update the profile image
        currentUser?.profileImage = url
        currentUser?.lastModified = today()

        if (currentUser != null) {
            repository.saveUserNetwork(currentUser!!) {
                Log.e(TAG, "updateProfileImage: profile image has been updated")
            }
        }
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
