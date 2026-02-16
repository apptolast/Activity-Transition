package io.rndev.paparcar.domain.usecase

import io.rndev.paparcar.domain.model.DrivingState
import io.rndev.paparcar.domain.model.Location
import io.rndev.paparcar.domain.repository.LocationSenderRepository
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

class SendLocationIfNeededUseCase(
    private val locationSenderRepository: LocationSenderRepository
) {
    private var lastLocationSentTime: Long = 0

    suspend operator fun invoke(drivingState: DrivingState, location: Location) {
        if (drivingState is DrivingState.Driving && location.accuracy < 20) {
            val now = Clock.System.now().toEpochMilliseconds()
            if (now - lastLocationSentTime > 1.minutes.inWholeMilliseconds) {
                locationSenderRepository.sendLocation(location)
                lastLocationSentTime = now
            }
        }
    }
}
