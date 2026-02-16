package io.rndev.paparcar.data.repository

import io.rndev.paparcar.domain.model.Location
import io.rndev.paparcar.domain.repository.LocationRepository
import io.rndev.paparcar.domain.service.LocationService
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImpl(
    private val locationService: LocationService
) : LocationRepository {

    override fun getLocationUpdates(): Flow<Location> {
        return locationService.getLocationUpdates()
    }
}
