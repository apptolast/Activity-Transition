package io.rndev.paparcar.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.rndev.paparcar.domain.model.DrivingState
import io.rndev.paparcar.presentation.intent.DrivingIntent
import io.rndev.paparcar.presentation.state.DrivingUiState
import io.rndev.paparcar.presentation.viewmodel.DrivingViewModel

@Composable
fun DrivingScreen() {
    val viewModel: DrivingViewModel = getViewModel()
    val uiState by viewModel.state.collectAsState()

    // This is a placeholder for permission handling. 
    // In a real app, you'd use a permission library.
    // For now, we'll assume permissions are granted.
    viewModel.handleIntent(DrivingIntent.OnPermissionsGranted)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val state = uiState) {
            is DrivingUiState.Loading -> {
                CircularProgressIndicator()
            }
            is DrivingUiState.PermissionsDenied -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Permissions are required to detect driving.")
                }
            }
            is DrivingUiState.Success -> {
                DrivingStatus(state.drivingState)
            }
            is DrivingUiState.Error -> {
                Text("Error: ${state.message}")
            }
        }
    }
}

@Composable
private fun DrivingStatus(drivingState: DrivingState) {
    val statusText = when (drivingState) {
        is DrivingState.Driving -> "Status: DRIVING"
        is DrivingState.NotDriving -> "Status: NOT DRIVING"
        is DrivingState.MaybeDriving -> "Status: MAYBE DRIVING (Confidence: ${drivingState.confidence})"
    }
    Text(text = statusText, modifier = Modifier.padding(16.dp))
}
