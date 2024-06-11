package my.id.jeremia.potholetracker.data.remote.apis.maps.route.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ComputeRouteRequest(
    @Json(name = "computeAlternativeRoutes")
    val computeAlternativeRoutes: Boolean? = false, // false
    @Json(name = "destination")
    val destination: Destination? ,
    @Json(name = "languageCode")
    val languageCode: String? = "id-ID", // en-US
    @Json(name = "origin")
    val origin: Origin? ,
    @Json(name = "routeModifiers")
    val routeModifiers: RouteModifiers? = null,
    @Json(name = "routingPreference")
    val routingPreference: String? = "TRAFFIC_UNAWARE", // TRAFFIC_AWARE
    @Json(name = "travelMode")
    val travelMode: String? = "DRIVE", // DRIVE
    @Json(name = "units")
    val units: String? = "IMPERIAL" // IMPERIAL
) {
    @JsonClass(generateAdapter = true)
    data class Destination(
        @Json(name = "placeId")
        val placeId: String
    )

    @JsonClass(generateAdapter = true)
    data class Origin(
        @Json(name = "location")
        val location: Location?
    ) {
        @JsonClass(generateAdapter = true)
        data class Location(
            @Json(name = "latLng")
            val latLng: LatLng?
        ) {
            @JsonClass(generateAdapter = true)
            data class LatLng(
                @Json(name = "latitude")
                val latitude: Double?,  // 37.419734
                @Json(name = "longitude")
                val longitude: Double?  // -122.0827784
            )
        }
    }

    @JsonClass(generateAdapter = true)
    data class RouteModifiers(
        @Json(name = "avoidFerries")
        val avoidFerries: Boolean? = null, // false
        @Json(name = "avoidHighways")
        val avoidHighways: Boolean? = null, // false
        @Json(name = "avoidTolls")
        val avoidTolls: Boolean? = null // false
    )
}