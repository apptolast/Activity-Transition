package io.rndev.paparcar.domain.usecase

import io.rndev.paparcar.domain.model.DrivingState
import io.rndev.paparcar.domain.model.Location
import io.rndev.paparcar.domain.repository.ActivityRepository
import io.rndev.paparcar.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObserveDrivingStateUseCase(
    private val activityRepository: ActivityRepository,
    private val locationRepository: LocationRepository,
    private val drivingStateEvaluator: DrivingStateEvaluator
) {
    operator fun invoke(): Flow<Pair<DrivingState, Location>> {
        return activityRepository.getActivityUpdates()
            .combine(locationRepository.getLocationUpdates()) { activity, location ->
                drivingStateEvaluator.evaluate(activity, location) to location
            }
    }
}
