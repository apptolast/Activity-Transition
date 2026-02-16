package io.rndev.paparcar.domain.model

data class Location(
    val latitude: Double,
    val longitude: Double,
    val speed: Float,
    val accuracy: Float
)
