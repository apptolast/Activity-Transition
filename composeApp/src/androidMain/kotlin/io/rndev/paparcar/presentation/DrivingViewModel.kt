package io.rndev.paparcar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.rndev.paparcar.domain.model.DrivingState
import io.rndev.paparcar.domain.usecase.ObserveDrivingStateUseCase
import io.rndev.paparcar.domain.usecase.SendLocationIfNeededUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DrivingViewModel(
    private val observeDrivingStateUseCase: ObserveDrivingStateUseCase,
    private val sendLocationIfNeededUseCase: SendLocationIfNeededUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DrivingUiState>(DrivingUiState.Loading)
    val uiState: StateFlow<DrivingUiState> = _uiState.asStateFlow()

    fun onPermissionsGranted() {
        observeDrivingStateUseCase()
            .onEach { (drivingState, location) ->
                _uiState.value = DrivingUiState.Success(drivingState)
                sendLocationIfNeededUseCase(drivingState, location)
            }
            .launchIn(viewModelScope)
    }

    fun onPermissionsDenied() {
        _uiState.value = DrivingUiState.PermissionsDenied
    }
}

sealed class DrivingUiState {
    object Loading : DrivingUiState()
    object PermissionsDenied : DrivingUiState()
    data class Success(val drivingState: DrivingState) : DrivingUiState()
    data class Error(val message: String) : DrivingUiState()
}
