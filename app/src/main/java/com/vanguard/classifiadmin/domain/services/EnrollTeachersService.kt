package com.vanguard.classifiadmin.domain.services

import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.vanguard.classifiadmin.MainActivity
import com.vanguard.classifiadmin.data.network.models.UserNetworkModel
import com.vanguard.classifiadmin.data.repository.MainRepository
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.ui.screens.welcome.CreateSchoolErrorState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

object EnrollTeachersServiceExtras {
    const val currentSchoolId = "currentSchoolIdEnrollTeachersServiceExtras"
}

object EnrollTeachersServiceActions {
    const val ACTION_UPLOAD = "action_upload"
    const val ACTION_ERROR = "action_error"
    const val ACTION_COMPLETED = "action_completed"
}

@AndroidEntryPoint
class EnrollTeachersService : BaseInsertionService() {
    @Inject
    lateinit var repository: MainRepository
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {
            if (intent?.action == EnrollTeachersServiceActions.ACTION_UPLOAD) {
                val currentSchoolId =
                    intent.getStringExtra(EnrollTeachersServiceExtras.currentSchoolId)!!
                enrollTeachers(currentSchoolId)
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private suspend fun enrollTeachers(schoolId: String) {
        val failedAuthentications = ArrayList<UserNetworkModel>()
        //get staged teachers
        repository.getStagedUsersNetwork(schoolId) { stagedTeachers ->
            //create new firebase accounts for each
            scope.launch {
                stagedTeachers.data?.map { stagedTeacher ->
                    repository.signUp(
                        stagedTeacher.email,
                        stagedTeacher.password,
                        onResult = { _, exception ->
                            //handle exception
                            if (exception.data == AuthExceptionState.UserAlreadyExists) {
                                if (!failedAuthentications.contains(stagedTeacher)) {
                                    failedAuthentications.add(stagedTeacher)
                                }
                            }
                            if (exception.data == AuthExceptionState.InvalidUserCredentials) {
                                if (!failedAuthentications.contains(stagedTeacher)) {
                                    failedAuthentications.add(stagedTeacher)
                                }
                            }
                            if (exception.data == AuthExceptionState.InvalidEmail) {
                                if (!failedAuthentications.contains(stagedTeacher)) {
                                    failedAuthentications.add(stagedTeacher)
                                }
                            }

                            if (exception.data == AuthExceptionState.NetworkProblem) {
                                if (!failedAuthentications.contains(stagedTeacher)) {
                                    failedAuthentications.add(stagedTeacher)
                                }
                            }
                        }
                    )
                }

                //save the teachers with successfully created accounts
                val successfullyCreated = mutableListOf<UserNetworkModel>()
                successfullyCreated.addAll(stagedTeachers.data ?: emptyList())
                successfullyCreated.removeAll(failedAuthentications)
                repository.saveUsersAsVerified(successfullyCreated) {

                    val message = when {
                        stagedTeachers.data?.isEmpty() == true ->
                            "Successfully enrolled a teacher!"

                        failedAuthentications.isEmpty() ->
                            "Successfully enrolled ${successfullyCreated.size} teachers!"

                        successfullyCreated.isEmpty() ->
                            "Could not enroll ${failedAuthentications.size} teachers"

                        else ->
                            "${successfullyCreated.size} teachers enrolled, ${failedAuthentications.size} failed."
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
                filter.addAction(EnrollTeachersServiceActions.ACTION_UPLOAD)
                filter.addAction(EnrollTeachersServiceActions.ACTION_COMPLETED)
                filter.addAction(EnrollTeachersServiceActions.ACTION_ERROR)
                return filter
            }
    }
}