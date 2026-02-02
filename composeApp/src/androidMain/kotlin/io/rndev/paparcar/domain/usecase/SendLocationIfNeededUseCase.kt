package io.rndev.paparcar.domain.usecase

import android.location.Location
import io.rndev.paparcar.domain.model.DrivingState
import io.rndev.paparcar.domain.repository.LocationSenderRepository
import java.util.concurrent.TimeUnit

class SendLocationIfNeededUseCase(
    private val locationSenderRepository: LocationSenderRepository
) {

    private var lastLocationSentTime: Long = 0

    suspend operator fun invoke(drivingState: DrivingState, location: Location) {
        if (drivingState is DrivingState.Driving && location.accuracy < 20) {
            val now = System.currentTimeMillis()
            if (now - lastLocationSentTime > TimeUnit.MINUTES.toMillis(1)) {
                locationSenderRepository.sendLocation(location)
                lastLocationSentTime = now
            }
        }
    }
}
