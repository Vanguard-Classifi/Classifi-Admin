package com.vanguard.classifiadmin.domain.services

import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.vanguard.classifiadmin.MainActivity
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.repository.MainRepository
import com.vanguard.classifiadmin.domain.services.ClassCreationServiceActions.ACTION_UPLOAD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


object ClassCreationServiceExtras {
    const val currentSchoolId = "currentSchoolIdClassCreationServiceExtras"
}

object ClassCreationServiceActions {
    const val ACTION_UPLOAD = "action_upload"
    const val ACTION_ERROR = "action_error"
    const val ACTION_COMPLETED = "action_completed"
}


@AndroidEntryPoint
class ClassCreationService : BaseInsertionService() {
    @Inject
    lateinit var repository: MainRepository
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {
            if (intent?.action == ACTION_UPLOAD) {
                val currentSchoolId =
                    intent.getStringExtra(ClassCreationServiceExtras.currentSchoolId)!!
                saveClassesAsVerified(currentSchoolId)
            }
        }

        return START_STICKY
    }

    private suspend fun saveClassesAsVerified(schoolId: String) {
        repository.getStagedClassesNetwork(schoolId) { classes ->
            scope.launch {
                delay(1000)
                if (classes.data != null) {
                    repository.saveClassesAsVerifiedNetwork(classes.data) {
                        onCompletionNotification(
                            "Successfully created ${classes.data.size} classes!",
                            true,
                        )
                    }
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun onCompletionNotification(caption: String, success: Boolean) {
        completedNotification(
            caption = caption,
            intent = Intent(this, MainActivity::class.java),
            success = success
        )
    }

    companion object {
        val intentFilter: IntentFilter
            get() {
                val filter = IntentFilter()
                filter.addAction(UploadServiceActions.ACTION_UPLOAD)
                filter.addAction(UploadServiceActions.ACTION_COMPLETED)
                filter.addAction(UploadServiceActions.ACTION_ERROR)
                return filter
            }
    }
}