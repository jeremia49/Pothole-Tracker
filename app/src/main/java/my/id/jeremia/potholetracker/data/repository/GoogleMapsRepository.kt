package my.id.jeremia.potholetracker.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import my.id.jeremia.potholetracker.data.remote.apis.maps.route.RouteAPI
import my.id.jeremia.potholetracker.data.remote.apis.maps.route.request.ComputeRouteRequest
import javax.inject.Inject


class GoogleMapsRepository @Inject constructor(
    private val routeAPI: RouteAPI
) {

    suspend fun getRoute(body: ComputeRouteRequest) = flow {
        emit(routeAPI.getRoute(body))
    }.flowOn(Dispatchers.IO)

}