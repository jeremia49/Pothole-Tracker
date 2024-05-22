package my.id.jeremia.potholetracker.utils.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientLocationRequest @Inject constructor(
    @ApplicationContext private val ctx: Context,
) {
    private val _fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)

    @SuppressLint("MissingPermission")
    fun startLocationUpdate(listener : (Location?)->Unit ) {

        val locationRequest = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setMinUpdateIntervalMillis(900)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                listener(locationResult.lastLocation)
            }
        }

        _fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())

    }

}