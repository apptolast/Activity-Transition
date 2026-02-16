package io.rndev.paparcar.data.mapper

import android.location.Location as AndroidLocation
import io.rndev.paparcar.domain.model.Location

class LocationMapper {
    fun toDomain(androidLocation: AndroidLocation): Location {
        return Location(
            latitude = androidLocation.latitude,
            longitude = androidLocation.longitude,
            speed = androidLocation.speed,
            accuracy = androidLocation.accuracy
        )
    }
}
