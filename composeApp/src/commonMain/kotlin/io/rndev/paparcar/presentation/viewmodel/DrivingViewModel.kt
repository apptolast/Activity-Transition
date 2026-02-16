package io.rndev.paparcar.presentation.viewmodel

import io.rndev.paparcar.domain.usecase.ObserveDrivingStateUseCase
import io.rndev.paparcar.domain.usecase.SendLocationIfNeededUseCase
import io.rndev.paparcar.presentation.intent.DrivingIntent
import io.rndev.paparcar.presentation.state.DrivingUiState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DrivingViewModel(
    private val observeDrivingStateUseCase: ObserveDrivingStateUseCase,
    private val sendLocationIfNeededUseCase: SendLocationIfNeededUseCase
) : BaseViewModel<DrivingUiState, DrivingIntent, Nothing>() {

    init {
        observeDrivingState()
    }

    override fun initState(): DrivingUiState = DrivingUiState.Loading

    override fun handleIntent(intent: DrivingIntent) {
        // Intent handling can be added here for other user actions if needed.
    }

    private fun observeDrivingState() {
        observeDrivingStateUseCase()
            .onEach { (drivingState, location) ->
                updateState { DrivingUiState.Success(drivingState) }
                sendLocationIfNeededUseCase(drivingState, location)
            }
            .catch { 
                // If an error occurs (e.g., permissions denied), update the state.
                updateState { DrivingUiState.PermissionsDenied }
            }
            .launchIn(viewModelScope)
    }
}
