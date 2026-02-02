package io.rndev.paparcar.domain.repository

import android.location.Location

interface LocationSenderRepository {
    suspend fun sendLocation(location: Location)
}
