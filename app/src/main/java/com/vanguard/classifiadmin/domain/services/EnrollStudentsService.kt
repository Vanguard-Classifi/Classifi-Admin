package com.vanguard.classifiadmin.domain.services

import android.content.Intent
import android.os.IBinder

class EnrollStudentsService : BaseInsertionService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}