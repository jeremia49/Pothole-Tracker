package my.id.jeremia.potholetracker.ui.HomeFind.NavigationView

import android.content.Context
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.data.local.db.entity.VerifiedInference
import my.id.jeremia.potholetracker.data.remote.apis.maps.route.request.ComputeRouteRequest
import my.id.jeremia.potholetracker.data.remote.apis.maps.route.response.ComputeRouteResponse
import my.id.jeremia.potholetracker.data.repository.GoogleMapsRepository
import my.id.jeremia.potholetracker.data.repository.VerifiedInferenceRepository
import my.id.jeremia.potholetracker.utils.googlemaps.HyperRect
import my.id.jeremia.potholetracker.utils.googlemaps.KDTree
import my.id.jeremia.potholetracker.utils.location.ClientLocationRequest
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    @ApplicationContext val ctx: Context,
    val locationRequest: ClientLocationRequest,
    val googleMapsRepository: GoogleMapsRepository,
    val inferenceRepository: VerifiedInferenceRepository
) : ViewModel() {

    companion object {
        const val TAG = "NavigationViewModel"
    }

    private val _inferencesBerlubang = mutableStateListOf<VerifiedInference>()
    val inferencesBerlubang : SnapshotStateList<VerifiedInference> = _inferencesBerlubang

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _locationData: MutableLiveData<Location> = MutableLiveData()
    val locationData: LiveData<Location>
        get() = _locationData

    private val _routeData: MutableLiveData<ComputeRouteResponse> = MutableLiveData()
    val routeData: LiveData<ComputeRouteResponse>
        get() = _routeData


    val placeID = mutableStateOf("")



    private val hyperRect: HyperRect = HyperRect(
        doubleArrayOf(-85.0, -180.0), doubleArrayOf(85.0, 180.0)
    )
    var kdTree : KDTree? = null;


    fun startLocationUpdates() {
        locationRequest.startLocationUpdate {
            if (it == null) return@startLocationUpdate
            _locationData.postValue(it)
        }
    }

    fun stopLocationUpdates() {
        locationRequest.stopLocationUpdate()
    }

    init {
        startLocationUpdates()


        viewModelScope.launch {
            val verifiedInference = inferenceRepository
                .fetchAll()
                .catch {
                    Log.e(TAG, "fetchAll: ", it)
                }
                .first()

            if (verifiedInference.isNotEmpty()) {
                _inferencesBerlubang.addAll(verifiedInference.filter{
                    it.status == "berlubang"
                })
            }


            kdTree = KDTree(
                _inferencesBerlubang.map{
                    doubleArrayOf(it.latitude.toDouble(), it.longitude.toDouble())
                }.toMutableList(), hyperRect
            )
        }

    }

    override fun onCleared() {
        stopLocationUpdates()
        super.onCleared()
    }

    fun startGetRoute(){
        viewModelScope.launch{
            val response = getRoute()
            _routeData.postValue(response)
            println(response)
        }
    }


    suspend fun getRoute() : ComputeRouteResponse {
        val request = ComputeRouteRequest(
            destination = ComputeRouteRequest.Destination(
                placeId = placeID.value,
            ),
            origin = ComputeRouteRequest.Origin(
                location = ComputeRouteRequest.Origin.Location(
                    latLng = ComputeRouteRequest.Origin.Location.LatLng(
                        longitude = _locationData.value!!.longitude,
                        latitude =  _locationData.value!!.latitude
                    )
                )
            )
        )

        val response = googleMapsRepository
            .getRoute(request)
            .first()

        return response

    }

}