package my.id.jeremia.potholetracker.Requests

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import my.id.jeremia.potholetracker.Data.LocationData
import my.id.jeremia.potholetracker.ViewModel.CollabViewModel



class LocationRequest(val ctx: Context) {

    private val _fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)

    @SuppressLint("MissingPermission")
    fun requestLocationUpdate(viewModel: CollabViewModel) {
        val locationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                viewModel.setGotLocationUpdate(true)
                print("true")
                locationResult.lastLocation?.let {
                    val locationData =
                        LocationData(
                            latitude = it.latitude,
                            longitude = it.longitude,
                            speed = it.speed,
                            accuracy = it.accuracy,
                            speedAccuracy = it.speedAccuracyMetersPerSecond,
                        )
                    viewModel.updateLocationData(locationData)
                }

                print("false")
            }

        }

        val locationRequest = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setMinUpdateIntervalMillis(900)
            .build()

        _fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())

    }
}