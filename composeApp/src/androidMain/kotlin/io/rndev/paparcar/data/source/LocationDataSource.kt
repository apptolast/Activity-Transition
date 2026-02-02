package io.rndev.paparcar.data.source

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationDataSource {
    fun getLocationUpdates(): Flow<Location>
}
