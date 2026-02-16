package io.rndev.paparcar.domain.repository

import io.rndev.paparcar.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationUpdates(): Flow<Location>
}