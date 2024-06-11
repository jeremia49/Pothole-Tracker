package my.id.jeremia.potholetracker.data.remote.apis.maps.route

import my.id.jeremia.potholetracker.data.remote.apis.maps.route.request.ComputeRouteRequest
import my.id.jeremia.potholetracker.data.remote.apis.maps.route.response.ComputeRouteResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RouteAPI {

    @POST(Endpoint.route)
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json",
        "X-Goog-FieldMask: routes.legs,routes.polyline",
    )
    suspend fun getRoute(
        @Body body:ComputeRouteRequest
    ):ComputeRouteResponse
}