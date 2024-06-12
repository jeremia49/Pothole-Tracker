package my.id.jeremia.potholetracker.utils.googlemaps

import kotlin.math.*

fun calculateBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val lat1Rad = Math.toRadians(lat1)
    val lon1Rad = Math.toRadians(lon1)
    val lat2Rad = Math.toRadians(lat2)
    val lon2Rad = Math.toRadians(lon2)

    val dLon = lon2Rad - lon1Rad

    val x = sin(dLon) * cos(lat2Rad)
    val y = cos(lat1Rad) * sin(lat2Rad) - (sin(lat1Rad) * cos(lat2Rad) * cos(dLon))

    var initialBearing = atan2(x, y)

    // Convert bearing from radians to degrees
    initialBearing = Math.toDegrees(initialBearing)
    val compassBearing = (initialBearing + 360) % 360

    return compassBearing
}

fun willPassBy(latA: Double, lonA: Double, latB: Double, lonB: Double, headingA: Float): Boolean {
    val bearingAB = calculateBearing(latA, lonA, latB, lonB)
    val angleDifference = abs(bearingAB - headingA)
    return angleDifference < 90 || angleDifference > 270
}