package io.rndev.paparcar.domain.repository

import io.rndev.paparcar.domain.model.Location

interface LocationSenderRepository {
    suspend fun sendLocation(location: Location)
}
