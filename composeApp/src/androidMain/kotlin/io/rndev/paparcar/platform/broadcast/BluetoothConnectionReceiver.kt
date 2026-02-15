package io.rndev.paparcar.platform.broadcast

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.rndev.paparcar.platform.service.LocationForegroundService

class BluetoothConnectionReceiver : BroadcastReceiver() {

    @SuppressLint("MissingPermission") // El permiso se declara en el Manifest
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == BluetoothDevice.ACTION_ACL_CONNECTED) {
            // Eliminamos el filtro por nombre de dispositivo por ahora.
            // El servicio se iniciará con cualquier conexión Bluetooth.
            LocationForegroundService.start(context)
        }
    }
}