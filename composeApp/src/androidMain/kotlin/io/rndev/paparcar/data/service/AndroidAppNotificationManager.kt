package io.rndev.paparcar.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import io.rndev.paparcar.R
import io.rndev.paparcar.domain.service.AppNotificationManager

class AndroidAppNotificationManager(private val context: Context) : AppNotificationManager {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val FOREGROUND_SERVICE_CHANNEL_ID = "paparcar_foreground_service_channel"
        const val ACTIVITY_TRANSITION_CHANNEL_ID = "paparcar_activity_transition_channel"
        private const val FOREGROUND_NOTIFICATION_ID = 1
        private const val ACTIVITY_TRANSITION_NOTIFICATION_ID = 2
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val foregroundChannel = NotificationChannel(
                FOREGROUND_SERVICE_CHANNEL_ID,
                "PaparCar Foreground Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val transitionChannel = NotificationChannel(
                ACTIVITY_TRANSITION_CHANNEL_ID,
                "Activity Transitions",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannels(listOf(foregroundChannel, transitionChannel))
        }
    }

    // This method is not on the interface, it's specific to the Android implementation
    // to bridge with the Foreground Service requirement.
    fun buildGpsTrackingInProgressNotification(): Notification {
        return buildNotification(
            title = "PaparCar tracking",
            content = "Getting precise GPS location...",
            showLoading = true
        )
    }

    override fun showGpsTrackingInProgressNotification() {
        notificationManager.notify(FOREGROUND_NOTIFICATION_ID, buildGpsTrackingInProgressNotification())
    }

    override fun showGpsTrackingSuccessNotification(latitude: Double, longitude: Double) {
        val content = "Lat: %.5f\nLon: %.5f".format(latitude, longitude)
        val notification = buildNotification(
            title = "Car location saved",
            content = content,
            showLoading = false
        )
        notificationManager.notify(FOREGROUND_NOTIFICATION_ID, notification)
    }

    override fun showGpsTrackingErrorNotification() {
        val notification = buildNotification(
            title = "Location error",
            content = "Unable to get GPS location",
            showLoading = false
        )
        notificationManager.notify(FOREGROUND_NOTIFICATION_ID, notification)
    }

    override fun showActivityTransitionNotification(activityName: String, transitionType: String) {
        val title = "Activity Transition Detected"
        val content = "Transition: $activityName - $transitionType"

        val notification = NotificationCompat.Builder(context, ACTIVITY_TRANSITION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // Dismiss on tap
            .build()

        notificationManager.notify(ACTIVITY_TRANSITION_NOTIFICATION_ID, notification)
    }

    override fun getForegroundServiceNotificationId(): Int = FOREGROUND_NOTIFICATION_ID

    private fun buildNotification(title: String, content: String, showLoading: Boolean): Notification {
        val builder = NotificationCompat.Builder(context, FOREGROUND_SERVICE_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setOnlyAlertOnce(true)

        if (showLoading) {
            builder.setProgress(0, 0, true)
        } else {
            builder.setProgress(0, 0, false)
        }

        return builder.build()
    }
}
