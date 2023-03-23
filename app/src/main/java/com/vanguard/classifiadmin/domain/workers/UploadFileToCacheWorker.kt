package com.vanguard.classifiadmin.domain.workers

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.vanguard.classifiadmin.data.repository.MainRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException
import javax.inject.Inject

const val ID_UPLOAD_FILE_TO_CACHE_WORKER = "id_upload_file_to_cache_worker"

@HiltWorker
class UploadFileToCacheWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: MainRepository,
) : CoroutineWorker(context, params) {
    private val url = inputData.getString(ID_UPLOAD_FILE_TO_CACHE_WORKER)

    override suspend fun doWork(): Result {
        return try {
            val uri = getUriFromUrl(url.orEmpty())
            val bitmap = repository.getBitmapFromUri(uri) ?: return Result.failure()
            repository.saveImageFileAndReturnUri(bitmap)
            Result.success()
        } catch (e: IOException) {
            Result.failure()
        }
    }

    private fun getUriFromUrl(url: String): Uri =
        Uri.parse(url)

    companion object {
        fun collectUri(uri: String): Data {
            val builder = Data.Builder()
                .putString(ID_UPLOAD_FILE_TO_CACHE_WORKER, uri)
            return builder.build()
        }
    }

}