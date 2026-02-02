package io.rndev.paparcar.domain.usecase

import android.location.Location
import io.rndev.paparcar.domain.model.ActivityType
import io.rndev.paparcar.domain.model.DrivingState

class DrivingStateEvaluator {

    private var currentState: DrivingState = DrivingState.NotDriving

    fun evaluate(activity: ActivityType, location: Location): DrivingState {
        val newDrivingState = when (currentState) {
            is DrivingState.NotDriving -> {
                if (activity == ActivityType.IN_VEHICLE || location.speed > 2.2) { // 8 km/h
                    DrivingState.MaybeDriving(0.5f)
                } else {
                    DrivingState.NotDriving
                }
            }
            is DrivingState.MaybeDriving -> {
                if (location.speed > 4.1) { // 15 km/h
                    DrivingState.Driving
                } else if (activity == ActivityType.STILL || activity == ActivityType.ON_FOOT) {
                    DrivingState.NotDriving
                } else {
                    currentState
                }
            }
            is DrivingState.Driving -> {
                if (location.speed < 0.3 && (activity == ActivityType.STILL || activity == ActivityType.ON_FOOT)) { // 1 km/h
                    DrivingState.NotDriving
                } else {
                    currentState
                }
            }
        }
        currentState = newDrivingState
        return newDrivingState
    }
}
