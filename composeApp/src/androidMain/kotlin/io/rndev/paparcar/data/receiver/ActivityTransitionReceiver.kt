package io.rndev.paparcar.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import io.rndev.paparcar.presentation.NotificationHelper
import kotlinx.coroutines.flow.MutableSharedFlow

class ActivityTransitionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("PaparcarApp", "[BroadcastReceiver] onReceive triggered.")
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)
            result?.let {
                val notificationHelper = NotificationHelper(context)

                it.transitionEvents.forEach { event ->
                    val activityName = mapActivity(event.activityType)
                    val transitionName = mapTransition(event.transitionType)
                    
                    val logMessage = "[BroadcastReceiver] Activity Event: Type=$activityName, Transition=$transitionName"
                    Log.d("PaparcarApp", logMessage)
                    _events.tryEmit(event)
                    
                    notificationHelper.showTransitionNotification(activityName, transitionName)
                }
            }
        } else {
            Log.w("PaparcarApp", "[BroadcastReceiver] Intent received with no ActivityTransitionResult.")
        }
    }

    private fun mapActivity(type: Int): String {
        return when (type) {
            DetectedActivity.IN_VEHICLE -> "IN_VEHICLE"
            DetectedActivity.STILL -> "STILL"
            DetectedActivity.ON_FOOT -> "ON_FOOT"
            DetectedActivity.WALKING -> "WALKING"
            DetectedActivity.RUNNING -> "RUNNING"
            else -> "UNKNOWN ($type)"
        }
    }

    private fun mapTransition(type: Int): String {
        return when (type) {
            ActivityTransition.ACTIVITY_TRANSITION_ENTER -> "ENTER"
            ActivityTransition.ACTIVITY_TRANSITION_EXIT -> "EXIT"
            else -> "UNKNOWN ($type)"
        }
    }

    companion object {
        private val _events = MutableSharedFlow<ActivityTransitionEvent>()
        val events = _events
    }
}
