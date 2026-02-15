package io.rndev.paparcar.platform.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import io.rndev.paparcar.presentation.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LocationForegroundService : Service() {

    // Aquí podrías inyectar tus UseCases del dominio con Koin
    // private val myUseCase: MyUseCase by inject()

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundProcess()

        // Aquí iniciarías la lógica de tu app, por ejemplo, el seguimiento de ubicación
        // myUseCase.execute()
        scheduleStop()
        // START_STICKY asegura que el servicio se reinicie si el sistema lo mata
        return START_STICKY
    }

    private fun startForegroundProcess() {
        val notificationHelper = NotificationHelper(this)
        val notification = notificationHelper.getForegroundServiceNotification()
        startForeground(NotificationHelper.FOREGROUND_NOTIFICATION_ID, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // No necesitamos binding para este caso
    }

    private fun scheduleStop() {
        serviceScope.launch {
            delay(60_000) // 1 minuto
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LocationForegroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }
}