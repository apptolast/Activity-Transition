package io.rndev.paparcar.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import io.rndev.paparcar.R

class NotificationHelper(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        // Channel for regular notifications
        const val ACTIVITY_TRANSITION_CHANNEL_ID = "paparcar_activity_channel"
        private const val ACTIVITY_TRANSITION_CHANNEL_NAME = "Activity Transitions"

        // Channel for the foreground service
        const val FOREGROUND_SERVICE_CHANNEL_ID = "paparcar_foreground_service_channel"
        private const val FOREGROUND_SERVICE_CHANNEL_NAME = "Paparcar Service"
        const val FOREGROUND_NOTIFICATION_ID = 1
    }

    /**
     * Creates all necessary notification channels for the app.
     * Should be called on app startup.
     */
    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Channel for transitions
            val activityChannel = NotificationChannel(
                ACTIVITY_TRANSITION_CHANNEL_ID,
                ACTIVITY_TRANSITION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for driving activity transitions"
            }
            notificationManager.createNotificationChannel(activityChannel)

            // Channel for foreground service
            val serviceChannel = NotificationChannel(
                FOREGROUND_SERVICE_CHANNEL_ID,
                FOREGROUND_SERVICE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW // Low importance for persistent notifications
            ).apply {
                description = "Shows when the app is actively tracking your location"
            }
            notificationManager.createNotificationChannel(serviceChannel)
        }
    }

    fun showTransitionNotification(activityName: String, transitionName: String) {
        val builder = NotificationCompat.Builder(context, ACTIVITY_TRANSITION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Use app icon
            .setContentTitle("Driving Activity Update")
            .setContentText("Activity: $activityName ($transitionName)")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, builder.build())
    }

    /**
     * Builds and returns the notification for the foreground service.
     * This notification is persistent and uses the low-priority channel.
     */
    fun getForegroundServiceNotification(): Notification {
        return NotificationCompat.Builder(context, FOREGROUND_SERVICE_CHANNEL_ID)
            .setContentTitle("Tracking location...")
            .setContentText("Getting your car location")
            .setSmallIcon(android.R.drawable.ic_menu_help)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setProgress(0, 0, true)
            .build()
    }
}
