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

object EnrollParentsServiceExtras {
    const val currentSchoolId = "currentSchoolIdEnrollParentsServiceExtras"
}

object EnrollParentsServiceActions {
    const val ACTION_UPLOAD = "action_upload"
    const val ACTION_ERROR = "action_error"
    const val ACTION_COMPLETED = "action_completed"
}

@AndroidEntryPoint
class EnrollParentsService : BaseInsertionService() {
    @Inject
    lateinit var repository: MainRepository
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {
            if (intent?.action == EnrollParentsServiceActions.ACTION_UPLOAD) {
                val currentSchoolId =
                    intent.getStringExtra(EnrollParentsServiceExtras.currentSchoolId)!!
                enrollParents(currentSchoolId)
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private suspend fun enrollParents(schoolId: String) {
        val failedAuthentications = ArrayList<UserNetworkModel>()

        repository.getStagedParentsNetwork(schoolId) { stagedParents ->
            scope.launch {
                stagedParents.data?.map { stagedParent ->
                    repository.signUp(
                        stagedParent.email,
                        stagedParent.password,
                        onResult = { _, exception ->
                            //handle exception
                            if (exception.data == AuthExceptionState.UserAlreadyExists) {
                                if (!failedAuthentications.contains(stagedParent)) {
                                    failedAuthentications.add(stagedParent)
                                }
                            }
                            if (exception.data == AuthExceptionState.InvalidUserCredentials) {
                                if (!failedAuthentications.contains(stagedParent)) {
                                    failedAuthentications.add(stagedParent)
                                }
                            }
                            if (exception.data == AuthExceptionState.InvalidEmail) {
                                if (!failedAuthentications.contains(stagedParent)) {
                                    failedAuthentications.add(stagedParent)
                                }
                            }

                            if (exception.data == AuthExceptionState.NetworkProblem) {
                                if (!failedAuthentications.contains(stagedParent)) {
                                    failedAuthentications.add(stagedParent)
                                }
                            }
                        }
                    )
                }

                //save the teachers with successfully created accounts
                val successfullyCreated = mutableListOf<UserNetworkModel>()
                successfullyCreated.addAll(stagedParents.data ?: emptyList())
                successfullyCreated.removeAll(failedAuthentications)

                repository.saveUsersAsVerified(successfullyCreated) {
                    val message = when {
                        stagedParents.data?.isEmpty() == true ->
                            "Successfully enrolled a parent!"

                        failedAuthentications.isEmpty() ->
                            "Successfully enrolled ${successfullyCreated.size} parents!"

                        successfullyCreated.isEmpty() ->
                            "Could not enroll ${failedAuthentications.size} parents"

                        successfullyCreated.size == 1 ->
                            "Successfully enrolled a parent!"

                        else ->
                            "${successfullyCreated.size} parents enrolled, ${failedAuthentications.size} failed."
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
                filter.addAction(EnrollParentsServiceActions.ACTION_UPLOAD)
                filter.addAction(EnrollParentsServiceActions.ACTION_COMPLETED)
                filter.addAction(EnrollParentsServiceActions.ACTION_ERROR)
                return filter
            }
    }
}