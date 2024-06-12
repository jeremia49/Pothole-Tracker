package my.id.jeremia.potholetracker.utils.googlemaps

import android.location.Location
data class distanceBearingResult(val distance: Float, val bearing: Float)

fun checkDistanceAndBearing(s1:Double,s2:Double,t1:Double,t2:Double):distanceBearingResult{
    val results = floatArrayOf(0f, 0f)
    Location.distanceBetween(
        s1,
        s2,
        t1,
        t2,
        results
    )
    val distance = results[0]
    val bearing = if (results.size > 2) {
        results[2]
    } else if (results.size > 1) {
        results[1]
    } else {
        0f
    }
    return distanceBearingResult(distance,bearing);
}