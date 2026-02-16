package io.rndev.paparcar.data.repository

import io.rndev.paparcar.domain.model.Location
import io.rndev.paparcar.domain.repository.LocationSenderRepository

class LocationSenderRepositoryImpl : LocationSenderRepository {

    override suspend fun sendLocation(location: Location) {
        // Simulate sending location to a server
        println("Sending location: ${location.latitude}, ${location.longitude}")
    }
}
