package io.rndev.paparcar.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.rndev.paparcar.domain.repository.LocationRepository
import io.rndev.paparcar.domain.service.AppNotificationManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
 import org.koin.core.context.GlobalContext.get

// DO NOT implement KoinComponent or use 'by inject()' here.
class LocationForegroundService : Service() {

    // Dependencies will be safely initialized in onCreate
    private lateinit var locationRepository: LocationRepository
    private lateinit var notificationManager: AppNotificationManager

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // CORRECT & SAFE way to get dependencies inside a Service.
        // At this point, Koin is guaranteed to be initialized.
        locationRepository = get().get()
        notificationManager = get().get()
        Log.d("LocationService", "Service instance created and dependencies injected.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notification = (notificationManager as? AndroidAppNotificationManager)?.buildGpsTrackingInProgressNotification()

        if (notification != null) {
            startForeground(notificationManager.getForegroundServiceNotificationId(), notification)
        } else {
            Log.e("LocationService", "Failed to create foreground notification. Stopping service.")
            stopSelf()
            return START_NOT_STICKY
        }

        // The service is now stable, we can launch the location fetching logic.
        fetchLocationAndStop(startId)

        // Use START_STICKY for services that are explicitly started and stopped.
        return START_STICKY
    }

    private fun fetchLocationAndStop(startId: Int) {
        serviceScope.launch {
            Log.d("LocationService", "Coroutine launched to fetch location...")

            val timeoutJob = launch {
                delay(30_000) // 30-second timeout
                Log.w("LocationService", "Location timeout reached. Updating notification and stopping service.")
                notificationManager.showGpsTrackingErrorNotification()
                stopSelf(startId)
            }

            try {
                // This code will now execute without crashing.
                val location = locationRepository.getLocationUpdates().first() // Wait for the first valid location
                timeoutJob.cancel() // Got the location, cancel the timeout

                Log.d("LocationService", "Location received: ${location.latitude}, ${location.longitude}")

                // THIS IS THE FIX: This call will now be executed.
                // It uses the same notification ID, so it UPDATES the existing one.
                notificationManager.showGpsTrackingSuccessNotification(location.latitude, location.longitude)

                // Optional: Keep the success notification visible for a few seconds before stopping.
                delay(10_000)

            } catch (e: Exception) {
                if (e is CancellationException) throw e // Rethrow cancellation
                timeoutJob.cancel()
                Log.e("LocationService", "Error getting location", e)
                notificationManager.showGpsTrackingErrorNotification()
            } finally {
                // Ensure the service stops itself after completing its work or failing.
                Log.d("LocationService", "Stopping service with startId: $startId")
                stopSelf(startId)
            }
        }
    }

    override fun onDestroy() {
        serviceScope.cancel() // Always cancel the scope to avoid leaks
        Log.d("LocationService", "Service destroyed.")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
