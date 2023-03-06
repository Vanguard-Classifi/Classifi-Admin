package com.vanguard.classifiadmin.domain.services

import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.vanguard.classifiadmin.MainActivity
import com.vanguard.classifiadmin.data.repository.MainRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


object SubjectCreationServiceExtras {
    const val currentSchoolId = "currentSchoolIdSubjectCreationServiceExtras"
}

object SubjectCreationServiceActions {
    const val ACTION_UPLOAD = "action_upload"
    const val ACTION_ERROR = "action_error"
    const val ACTION_COMPLETED = "action_completed"
}



@AndroidEntryPoint
class SubjectCreationService: BaseInsertionService() {
    @Inject
    lateinit var repository: MainRepository
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {
            if (intent?.action == SubjectCreationServiceActions.ACTION_UPLOAD) {
                val currentSchoolId =
                    intent.getStringExtra(SubjectCreationServiceExtras.currentSchoolId)!!
                saveSubjectsAsVerified(currentSchoolId)
            }
        }

        return START_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private suspend fun saveSubjectsAsVerified(schoolId: String) {
        repository.getStagedSubjectsNetwork(schoolId) { subjects ->
            scope.launch {
                delay(1000)
                if (subjects.data != null) {
                    repository.saveSubjectsAsVerifiedNetwork(subjects.data) {
                        onCompletionNotification(
                            caption = if (subjects.data.isEmpty()) "Successfully created a subject!" else
                                "Successfully created ${subjects.data.size} subjects!",
                            true,
                        )
                    }
                }
            }
        }
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
                filter.addAction(SubjectCreationServiceActions.ACTION_UPLOAD)
                filter.addAction(SubjectCreationServiceActions.ACTION_COMPLETED)
                filter.addAction(SubjectCreationServiceActions.ACTION_ERROR)
                return filter
            }
    }
}