package my.id.jeremia.potholetracker.utils.googlemaps

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class InferencePoint(private val latLng: LatLng) : ClusterItem{
    override fun getPosition(): LatLng {
        return latLng
    }

    override fun getTitle(): String {
        return "Berlubang"
    }

    override fun getSnippet(): String?{
        return null
    }

    override fun getZIndex(): Float {
        return 1f
    }
}