package com.vanguard.classifiadmin.domain.services

import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.vanguard.classifiadmin.MainActivity
import com.vanguard.classifiadmin.data.network.models.UserNetworkModel
import com.vanguard.classifiadmin.data.repository.MainRepository
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


object EnrollStudentsServiceExtras {
    const val currentSchoolId = "currentSchoolIdEnrollStudentsServiceExtras"
}

object EnrollStudentsServiceActions {
    const val ACTION_UPLOAD = "action_upload"
    const val ACTION_ERROR = "action_error"
    const val ACTION_COMPLETED = "action_completed"
}

@AndroidEntryPoint
class EnrollStudentsService : BaseInsertionService() {
    @Inject
    lateinit var repository: MainRepository
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {
            if (intent?.action == EnrollStudentsServiceActions.ACTION_UPLOAD) {
                val currentSchoolId =
                    intent.getStringExtra(EnrollStudentsServiceExtras.currentSchoolId)!!
                enrollStudents(currentSchoolId)
            }
        }
        return START_STICKY
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

    private suspend fun enrollStudents(schoolId: String) {
        val failedAuthentications = ArrayList<UserNetworkModel>()

        repository.getStagedStudentsNetwork(schoolId) { stagedStudents ->
            scope.launch {
                stagedStudents.data?.map { stagedStudent ->
                    repository.signUp(
                        stagedStudent.email,
                        stagedStudent.password,
                        onResult = { _, exception ->
                            //handle exception
                            if (exception.data == AuthExceptionState.UserAlreadyExists) {
                                if (!failedAuthentications.contains(stagedStudent)) {
                                    failedAuthentications.add(stagedStudent)
                                }
                            }
                            if (exception.data == AuthExceptionState.InvalidUserCredentials) {
                                if (!failedAuthentications.contains(stagedStudent)) {
                                    failedAuthentications.add(stagedStudent)
                                }
                            }
                            if (exception.data == AuthExceptionState.InvalidEmail) {
                                if (!failedAuthentications.contains(stagedStudent)) {
                                    failedAuthentications.add(stagedStudent)
                                }
                            }

                            if (exception.data == AuthExceptionState.NetworkProblem) {
                                if (!failedAuthentications.contains(stagedStudent)) {
                                    failedAuthentications.add(stagedStudent)
                                }
                            }
                        }
                    )
                }

                //save the teachers with successfully created accounts
                val successfullyCreated = mutableListOf<UserNetworkModel>()
                successfullyCreated.addAll(stagedStudents.data ?: emptyList())
                successfullyCreated.removeAll(failedAuthentications)

                repository.saveUsersAsVerified(successfullyCreated) {
                    val message = when {
                        stagedStudents.data?.isEmpty() == true ->
                            "Successfully enrolled a student!"

                        failedAuthentications.isEmpty() ->
                            "Successfully enrolled ${successfullyCreated.size} students!"

                        successfullyCreated.isEmpty() ->
                            "Could not enroll ${failedAuthentications.size} students"

                        successfullyCreated.size == 1 ->
                            "Successfully enrolled a student!"

                        else ->
                            "${successfullyCreated.size} students enrolled, ${failedAuthentications.size} failed."
                    }

                    val state = when {
                        failedAuthentications.isEmpty() -> true
                        successfullyCreated.isEmpty() -> false
                        else -> true
                    }

                    onCompletionNotification(
                        caption = message,
                        success = state
                    )
                }

            }
        }
    }

    companion object {
        val intentFilter: IntentFilter
            get() {
                val filter = IntentFilter()
                filter.addAction(EnrollStudentsServiceActions.ACTION_UPLOAD)
                filter.addAction(EnrollStudentsServiceActions.ACTION_COMPLETED)
                filter.addAction(EnrollStudentsServiceActions.ACTION_ERROR)
                return filter
            }
    }
}