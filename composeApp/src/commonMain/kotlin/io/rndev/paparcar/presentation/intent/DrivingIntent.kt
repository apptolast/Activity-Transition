package io.rndev.paparcar.presentation.intent

sealed class DrivingIntent {
    object OnPermissionsGranted : DrivingIntent()
    object OnPermissionsDenied : DrivingIntent()
}
