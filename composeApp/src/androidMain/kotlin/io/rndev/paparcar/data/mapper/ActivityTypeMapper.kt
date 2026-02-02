package io.rndev.paparcar.data.mapper

import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.DetectedActivity
import io.rndev.paparcar.domain.model.ActivityType

class ActivityTypeMapper {
    fun toActivityType(event: ActivityTransitionEvent): ActivityType {
        return when (event.activityType) {
            DetectedActivity.IN_VEHICLE -> ActivityType.IN_VEHICLE
            DetectedActivity.STILL -> ActivityType.STILL
            DetectedActivity.ON_FOOT -> ActivityType.ON_FOOT
            else -> ActivityType.UNKNOWN
        }
    }
}
