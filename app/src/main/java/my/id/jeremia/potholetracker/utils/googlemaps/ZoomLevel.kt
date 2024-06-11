package my.id.jeremia.potholetracker.utils.googlemaps

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

object ZoomLevel {

    fun calculateZoomLevel(point1: LatLng, point2: LatLng, screenWidth: Int, screenHeight: Int): Int {
        val builder = LatLngBounds.Builder()
        builder.include(point1)
        builder.include(point2)
        val bounds = builder.build()

        val padding = 100 // adjust as needed
        val width = screenWidth - padding
        val height = screenHeight - padding

        return getZoomLevel(bounds, width, height)
    }

    private fun getZoomLevel(bounds: LatLngBounds, width: Int, height: Int): Int {
        val GLOBE_WIDTH = 256 // a constant in Google's map projection
        var zoomLevel = 1
        if (bounds != null) {
            val diffLat = (bounds.northeast.latitude - bounds.southwest.latitude).toInt()
            val diffLng = (bounds.northeast.longitude - bounds.southwest.longitude).toInt()

            val latFraction = Math.abs(bounds.northeast.latitude - bounds.southwest.latitude) / 180
            val lngFraction = Math.abs(bounds.northeast.longitude - bounds.southwest.longitude) / 360

            val latZoom = Math.log((width / GLOBE_WIDTH / latFraction).toDouble()) / Math.log(2.0)
            val lngZoom = Math.log((height / GLOBE_WIDTH / lngFraction).toDouble()) / Math.log(2.0)

            zoomLevel = Math.min(Math.min(latZoom, lngZoom), Math.min(diffLat.toDouble(), diffLng.toDouble())).toInt()
        }
        return zoomLevel
    }
}
