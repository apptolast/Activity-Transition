package io.rndev.paparcar.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import io.rndev.paparcar.domain.service.AppNotificationManager
import org.koin.java.KoinJavaComponent.getKoin

// DO NOT implement KoinComponent or inject properties here.
class ActivityTransitionReceiver : BroadcastReceiver() {

    init {
        // This log should now appear in Logcat as the crash during instantiation is fixed.
        Log.d("ActivityTransitionReceiver", "Receiver instance created successfully.")
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ActivityTransitionReceiver", "onReceive called")

        // Safely get dependencies inside onReceive, not as a class property.
        // At this point, Koin is guaranteed to be initialized.
        val notificationManager: AppNotificationManager = getKoin().get()

        if (ActivityTransitionResult.hasResult(intent)) {
            Log.d("ActivityTransitionReceiver", "onReceive: ActivityTransitionResult.hasResult(intent)")

            val result = ActivityTransitionResult.extractResult(intent)
            result?.transitionEvents?.forEach { event ->
                Log.d("ActivityTransitionReceiver", "onReceive: event.activityType = ${event.activityType}, event.transitionType = ${event.transitionType}")
                ActivityTransitionEventSource.postEvent(event)

                val activityName = activityTypeToString(event.activityType)
                val transitionName = transitionTypeToString(event.transitionType)
                notificationManager.showActivityTransitionNotification(activityName, transitionName)
            }
        }
    }

    private fun activityTypeToString(activityType: Int): String {
        return when (activityType) {
            DetectedActivity.IN_VEHICLE -> "In Vehicle"
            DetectedActivity.ON_BICYCLE -> "On Bicycle"
            DetectedActivity.ON_FOOT -> "On Foot"
            DetectedActivity.STILL -> "Still"
            DetectedActivity.WALKING -> "Walking"
            DetectedActivity.RUNNING -> "Running"
            else -> "Unknown"
        }
    }

    private fun transitionTypeToString(transitionType: Int): String {
        return when (transitionType) {
            ActivityTransition.ACTIVITY_TRANSITION_ENTER -> "Enter"
            ActivityTransition.ACTIVITY_TRANSITION_EXIT -> "Exit"
            else -> "Unknown"
        }
    }
}
