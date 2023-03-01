package com.vanguard.classifiadmin.domain.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.vanguard.classifiadmin.R

abstract class BaseAvatarService : Service() {
    private var pendingTasks = 0

    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun createDefaultNotificationChannel() {
        val channel = NotificationChannel(
            DEFAULT_NOTIFICATION_CHANNEL,
            "Default",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    protected fun progressNotification(
        caption: String,
        completed: Long,
        total: Long
    ) {
        var percent = 0
        if (total > 0) {
            percent = (100 * completed / total).toInt()
        }

        createDefaultNotificationChannel()

        val builder = NotificationCompat.Builder(this, DEFAULT_NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.icon_branding_big)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(caption)
            .setProgress(100, percent, false)
            .setOngoing(true)
            .setAutoCancel(false)

        notificationManager.notify(PROGRESS_NOTIFICATION_ID, builder.build())
    }


    protected fun completedNotification(
        caption: String,
        intent: Intent,
        success: Boolean,
    ) {
        val flag = PendingIntent.FLAG_IMMUTABLE
        val pi = PendingIntent.getActivity(this, 0, intent, flag)
        val icon = if (success) R.drawable.icon_tick else R.drawable.icon_error

        createDefaultNotificationChannel()

        val builder = NotificationCompat.Builder(this, DEFAULT_NOTIFICATION_CHANNEL)
            .setSmallIcon(icon)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(caption)
            .setAutoCancel(true)
            .setContentIntent(pi)

        notificationManager.notify(FINISHED_NOTIFICATION_ID, builder.build())
    }

    protected fun dismissProgressNotification() {
        notificationManager.cancel(PROGRESS_NOTIFICATION_ID)
    }



    companion object {
        private const val DEFAULT_NOTIFICATION_CHANNEL = "default_notification_channel"
        internal const val PROGRESS_NOTIFICATION_ID = 4295
        internal const val FINISHED_NOTIFICATION_ID = 1232
    }
}