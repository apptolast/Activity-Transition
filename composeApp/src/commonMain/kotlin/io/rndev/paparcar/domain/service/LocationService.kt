package io.rndev.paparcar.domain.service

import io.rndev.paparcar.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationService {
    fun getLocationUpdates(): Flow<Location>
}
