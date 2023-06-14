package com.vanguard.classifiadmin.workers

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.storage.StorageReference
import com.khalidtouch.chatme.domain.repository.SchoolRepository
import com.khalidtouch.core.common.ClassifiDispatcher
import com.khalidtouch.core.common.Dispatcher
import com.vanguard.classifiadmin.network.constants.CloudStoreConstants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

const val KEY_SCHOOL_BANNER_FILE_URL = "KEY_SCHOOL_BANNER_FILE_URL"
const val KEY_MY_SCHOOL_ID = "KEY_MY_SCHOOL_ID"

@HiltWorker
class UploadSchoolBannerWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val schoolRepository: SchoolRepository,
    @Dispatcher(ClassifiDispatcher.IO) private val coroutineDispatcher: CoroutineDispatcher,
) : CoroutineWorker(context, workerParams) {
    val TAG = "UploadSchoolBanner"
    private val params = workerParams
    val scope = CoroutineScope(coroutineDispatcher + SupervisorJob())

    override suspend fun doWork(): Result {
        return try {
            val bannerUrl = checkNotNull(params.inputData.getString(KEY_SCHOOL_BANNER_FILE_URL))
            val schoolId = checkNotNull(params.inputData.getLong(KEY_MY_SCHOOL_ID, -1L))
            val bannerFile = Uri.parse(bannerUrl)
            val storageRef = CloudStoreConstants.School.schoolBannerRef(
                schoolId,
                checkNotNull(bannerFile.lastPathSegment)
            )
            val currentSchool = schoolRepository.fetchSchoolById(schoolId)
            Log.e(TAG, "doWork: current school is ${checkNotNull(currentSchool)}")
            val uploadTask = storageRef.putFile(bannerFile)
            uploadTask
                .addOnFailureListener {

                }.addOnSuccessListener { _ ->
                    storageRef.downloadUrl.addOnCompleteListener { result ->
                        Log.e(TAG, "doWork: the task result is ${result.result.toString()}")
                        scope.launch {
                            Log.e(TAG, "doWork: starting download")
                            storageRef.downloadBannerFile(
                                filename = checkNotNull(bannerFile.lastPathSegment),
                                callback = { bannerUrl ->
                                    //save to db
                                    Log.e(TAG, "doWork: download url is $bannerUrl")
                                    currentSchool.bannerImage = bannerUrl

                                    scope.launch {
                                        schoolRepository.updateSchool(
                                            school = currentSchool
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

private suspend fun StorageReference.downloadBannerFile(filename: String, callback: (String) -> Unit) {
    val localFile = withContext(Dispatchers.IO) {
        File.createTempFile(filename, "jpg")
    }
    this.getFile(localFile)
        .addOnFailureListener {
        }
        .addOnSuccessListener { snapshot ->
            snapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                callback(uri.toString())
            }
        }
}