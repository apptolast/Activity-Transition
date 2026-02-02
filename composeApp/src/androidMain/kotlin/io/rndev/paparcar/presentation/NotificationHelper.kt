package io.rndev.paparcar.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID = "paparcar_activity_channel"
        private const val CHANNEL_NAME = "Activity Transitions"
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for driving activity transitions"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showTransitionNotification(activityName: String, transitionName: String) {
        // On Android 13+, you need to have the POST_NOTIFICATIONS permission.
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // TODO: Replace with a proper app icon
            .setContentTitle("Driving Activity Update")
            .setContentText("Activity: $activityName ($transitionName)")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationId = System.currentTimeMillis().toInt()

        notificationManager.notify(notificationId, builder.build())
    }
}
