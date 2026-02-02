package io.rndev.paparcar.data.repository

import android.location.Location
import io.rndev.paparcar.data.source.LocationDataSource
import io.rndev.paparcar.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImpl(
    private val locationDataSource: LocationDataSource
) : LocationRepository {

    override fun getLocationUpdates(): Flow<Location> {
        return locationDataSource.getLocationUpdates()
    }
}
