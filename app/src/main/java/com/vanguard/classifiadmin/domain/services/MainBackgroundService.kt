package com.vanguard.classifiadmin.domain.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.vanguard.classifiadmin.data.preferences.PrefDatastore
import com.vanguard.classifiadmin.data.repository.MainRepository
import com.vanguard.classifiadmin.domain.helpers.IntentExtras
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainBackgroundService : Service() {
    @Inject
    lateinit var repository: MainRepository

    @Inject
    lateinit var store: PrefDatastore
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val currentUserEmail = intent?.getStringExtra(IntentExtras.currentUserEmail)

        //find user by email
        if(currentUserEmail != null) {
            scope.launch {
                repository.getUserByEmailNetwork(currentUserEmail ?: "") { user ->
                    //save user id to pref
                    val currentUser = user.data
                    store.saveCurrentUserIdPref(currentUser?.userId ?: "") {}
                    //save user name to pref
                    store.saveCurrentUsernamePref(currentUser?.fullname ?: "") {}
                    //save school id to pref
                    store.saveCurrentSchoolIdPref(currentUser?.currentSchoolId ?: "") {}
                    //save school name to pref
                    store.saveCurrentSchoolNamePref(currentUser?.currentSchoolName ?: "") {}

                }
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}