package io.rndev.paparcar.presentation.state

import io.rndev.paparcar.domain.model.DrivingState

sealed class DrivingUiState {
    object Loading : DrivingUiState()
    object PermissionsDenied : DrivingUiState()
    data class Success(val drivingState: DrivingState) : DrivingUiState()
    data class Error(val message: String) : DrivingUiState()
}
