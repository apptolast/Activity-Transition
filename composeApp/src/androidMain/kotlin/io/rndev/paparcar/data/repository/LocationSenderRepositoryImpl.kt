package io.rndev.paparcar.data.repository

import android.location.Location
import android.util.Log
import io.rndev.paparcar.domain.repository.LocationSenderRepository

class LocationSenderRepositoryImpl : LocationSenderRepository {

    override suspend fun sendLocation(location: Location) {
        // Simulate sending location to a server
        Log.d("LocationSender", "Sending location: ${location.latitude}, ${location.longitude}")
    }
}
