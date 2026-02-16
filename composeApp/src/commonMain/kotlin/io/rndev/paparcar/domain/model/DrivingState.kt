package io.rndev.paparcar.domain.model

sealed class DrivingState {
    object NotDriving : DrivingState()
    data class MaybeDriving(val confidence: Float) : DrivingState()
    object Driving : DrivingState()
}
