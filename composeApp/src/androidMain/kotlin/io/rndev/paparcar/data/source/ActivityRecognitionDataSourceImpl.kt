package io.rndev.paparcar.data.source

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity
import io.rndev.paparcar.data.mapper.ActivityTypeMapper
import io.rndev.paparcar.data.receiver.ActivityTransitionReceiver
import io.rndev.paparcar.domain.model.ActivityType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ActivityRecognitionDataSourceImpl(
    private val context: Context,
    private val mapper: ActivityTypeMapper
) : ActivityRecognitionDataSource {

    private val activityClient = ActivityRecognition.getClient(context)

    @SuppressLint("MissingPermission")
    override fun getActivityUpdates(): Flow<ActivityType> {
        Log.d("PaparcarApp", "ActivityRecognitionDataSource: Requesting activity updates.")

        val transitions = listOf(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_FOOT)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_FOOT)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build(),
            )

        val request = ActivityTransitionRequest(transitions)

        val intent = Intent(context, ActivityTransitionReceiver::class.java).apply {
            action = "io.rndev.paparcar.TRANSITION_RECEIVER_ACTION"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        activityClient.requestActivityTransitionUpdates(request, pendingIntent)
            .addOnSuccessListener {
                Log.d("PaparcarApp", "Activity updates requested successfully.")
            }
            .addOnFailureListener { e ->
                Log.e("PaparcarApp", "Failed to request activity updates.", e)
            }

        return ActivityTransitionReceiver.events.map { event ->
            mapper.toActivityType(event)
        }
    }
}
