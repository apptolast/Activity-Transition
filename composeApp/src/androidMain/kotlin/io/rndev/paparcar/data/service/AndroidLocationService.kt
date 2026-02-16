package io.rndev.paparcar.data.service

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import io.rndev.paparcar.data.mapper.LocationMapper
import io.rndev.paparcar.domain.model.Location
import io.rndev.paparcar.domain.service.LocationService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AndroidLocationService(
    context: Context,
    private val locationMapper: LocationMapper
) : LocationService {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(): Flow<Location> = callbackFlow {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000
        ).build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let {
                    trySend(locationMapper.toDomain(it))
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        ).addOnSuccessListener {
            Log.d("PaparcarApp", "Location updates requested successfully.")
        }.addOnFailureListener { e ->
            Log.e("PaparcarApp", "Failed to request location updates.", e)
        }

        awaitClose { 
            Log.d("PaparcarApp", "Location updates stopped.")
            fusedLocationClient.removeLocationUpdates(locationCallback) 
        }
    }
}
