package my.id.jeremia.potholetracker.data.remote.apis.maps.route.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ComputeRouteResponse(
    @Json(name = "geocodingResults")
    val geocodingResults: GeocodingResults?,
    @Json(name = "routes")
    val routes: List<Route?>?
) {
    @JsonClass(generateAdapter = true)
    class GeocodingResults

    @JsonClass(generateAdapter = true)
    data class Route(
        @Json(name = "description")
        val description: String?,
        @Json(name = "distanceMeters")
        val distanceMeters: Int?,
        @Json(name = "duration")
        val duration: String?,
        @Json(name = "legs")
        val legs: List<Leg?>?,
        @Json(name = "localizedValues")
        val localizedValues: LocalizedValues?,
        @Json(name = "polyline")
        val polyline: Polyline?,
        @Json(name = "routeLabels")
        val routeLabels: List<String?>?,
        @Json(name = "routeToken")
        val routeToken: String?,
        @Json(name = "staticDuration")
        val staticDuration: String?,
        @Json(name = "travelAdvisory")
        val travelAdvisory: TravelAdvisory?,
        @Json(name = "viewport")
        val viewport: Viewport?
    ) {
        @JsonClass(generateAdapter = true)
        data class Leg(
            @Json(name = "distanceMeters")
            val distanceMeters: Int?,
            @Json(name = "duration")
            val duration: String?,
            @Json(name = "endLocation")
            val endLocation: EndLocation?,
            @Json(name = "localizedValues")
            val localizedValues: LocalizedValues?,
            @Json(name = "polyline")
            val polyline: Polyline?,
            @Json(name = "startLocation")
            val startLocation: StartLocation?,
            @Json(name = "staticDuration")
            val staticDuration: String?,
            @Json(name = "steps")
            val steps: List<Step?>?
        ) {
            @JsonClass(generateAdapter = true)
            data class EndLocation(
                @Json(name = "latLng")
                val latLng: LatLng?
            ) {
                @JsonClass(generateAdapter = true)
                data class LatLng(
                    @Json(name = "latitude")
                    val latitude: Double?,
                    @Json(name = "longitude")
                    val longitude: Double?
                )
            }

            @JsonClass(generateAdapter = true)
            data class LocalizedValues(
                @Json(name = "distance")
                val distance: Distance?,
                @Json(name = "duration")
                val duration: Duration?,
                @Json(name = "staticDuration")
                val staticDuration: StaticDuration?
            ) {
                @JsonClass(generateAdapter = true)
                data class Distance(
                    @Json(name = "text")
                    val text: String?
                )

                @JsonClass(generateAdapter = true)
                data class Duration(
                    @Json(name = "text")
                    val text: String?
                )

                @JsonClass(generateAdapter = true)
                data class StaticDuration(
                    @Json(name = "text")
                    val text: String?
                )
            }

            @JsonClass(generateAdapter = true)
            data class Polyline(
                @Json(name = "encodedPolyline")
                val encodedPolyline: String?
            )

            @JsonClass(generateAdapter = true)
            data class StartLocation(
                @Json(name = "latLng")
                val latLng: LatLng?
            ) {
                @JsonClass(generateAdapter = true)
                data class LatLng(
                    @Json(name = "latitude")
                    val latitude: Double?,
                    @Json(name = "longitude")
                    val longitude: Double?
                )
            }

            @JsonClass(generateAdapter = true)
            data class Step(
                @Json(name = "distanceMeters")
                val distanceMeters: Int?,
                @Json(name = "endLocation")
                val endLocation: EndLocation?,
                @Json(name = "localizedValues")
                val localizedValues: LocalizedValues?,
                @Json(name = "navigationInstruction")
                val navigationInstruction: NavigationInstruction?,
                @Json(name = "polyline")
                val polyline: Polyline?,
                @Json(name = "startLocation")
                val startLocation: StartLocation?,
                @Json(name = "staticDuration")
                val staticDuration: String?,
                @Json(name = "travelMode")
                val travelMode: String?
            ) {
                @JsonClass(generateAdapter = true)
                data class EndLocation(
                    @Json(name = "latLng")
                    val latLng: LatLng?
                ) {
                    @JsonClass(generateAdapter = true)
                    data class LatLng(
                        @Json(name = "latitude")
                        val latitude: Double?,
                        @Json(name = "longitude")
                        val longitude: Double?
                    )
                }

                @JsonClass(generateAdapter = true)
                data class LocalizedValues(
                    @Json(name = "distance")
                    val distance: Distance?,
                    @Json(name = "staticDuration")
                    val staticDuration: StaticDuration?
                ) {
                    @JsonClass(generateAdapter = true)
                    data class Distance(
                        @Json(name = "text")
                        val text: String?
                    )

                    @JsonClass(generateAdapter = true)
                    data class StaticDuration(
                        @Json(name = "text")
                        val text: String?
                    )
                }

                @JsonClass(generateAdapter = true)
                data class NavigationInstruction(
                    @Json(name = "instructions")
                    val instructions: String?,
                    @Json(name = "maneuver")
                    val maneuver: String?
                )

                @JsonClass(generateAdapter = true)
                data class Polyline(
                    @Json(name = "encodedPolyline")
                    val encodedPolyline: String?
                )

                @JsonClass(generateAdapter = true)
                data class StartLocation(
                    @Json(name = "latLng")
                    val latLng: LatLng?
                ) {
                    @JsonClass(generateAdapter = true)
                    data class LatLng(
                        @Json(name = "latitude")
                        val latitude: Double?,
                        @Json(name = "longitude")
                        val longitude: Double?
                    )
                }
            }
        }

        @JsonClass(generateAdapter = true)
        data class LocalizedValues(
            @Json(name = "distance")
            val distance: Distance?,
            @Json(name = "duration")
            val duration: Duration?,
            @Json(name = "staticDuration")
            val staticDuration: StaticDuration?
        ) {
            @JsonClass(generateAdapter = true)
            data class Distance(
                @Json(name = "text")
                val text: String?
            )

            @JsonClass(generateAdapter = true)
            data class Duration(
                @Json(name = "text")
                val text: String?
            )

            @JsonClass(generateAdapter = true)
            data class StaticDuration(
                @Json(name = "text")
                val text: String?
            )
        }

        @JsonClass(generateAdapter = true)
        data class Polyline(
            @Json(name = "encodedPolyline")
            val encodedPolyline: String?
        )

        @JsonClass(generateAdapter = true)
        class TravelAdvisory

        @JsonClass(generateAdapter = true)
        data class Viewport(
            @Json(name = "high")
            val high: High?,
            @Json(name = "low")
            val low: Low?
        ) {
            @JsonClass(generateAdapter = true)
            data class High(
                @Json(name = "latitude")
                val latitude: Double?,
                @Json(name = "longitude")
                val longitude: Double?
            )

            @JsonClass(generateAdapter = true)
            data class Low(
                @Json(name = "latitude")
                val latitude: Double?,
                @Json(name = "longitude")
                val longitude: Double?
            )
        }
    }
}