package io.rndev.paparcar

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import io.rndev.paparcar.presentation.DrivingUiState
import io.rndev.paparcar.presentation.DrivingViewModel
import io.rndev.paparcar.presentation.NotificationHelper
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: DrivingViewModel by viewModel()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            viewModel.onPermissionsGranted()
        } else {
            viewModel.onPermissionsDenied()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the notification channel on app startup
        NotificationHelper(this).createNotificationChannels()
        requestPermissions()

        setContent {
            App {
                val uiState by viewModel.uiState.collectAsState()
                when (val state = uiState) {
                    is DrivingUiState.Loading -> Text("Requesting permissions...")
                    is DrivingUiState.PermissionsDenied -> Text("Permissions denied. Please grant permissions in settings.")
                    is DrivingUiState.Success -> Text("Driving state: ${state.drivingState}")
                    is DrivingUiState.Error -> Text("Error: ${state.message}")
                }
            }
        }
    }

    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACTIVITY_RECOGNITION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionsToRequest.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
    }
}
