package io.rndev.paparcar.domain.model

import java.util.Date

data class DrivingStatus(
    val state: DrivingState,
    val lastStateChange: Date,
    val lastLocationSent: Date?
)
