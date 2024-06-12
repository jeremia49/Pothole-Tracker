package my.id.jeremia.potholetracker.ui.HomeFind

import Message
import android.Manifest
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.maps.android.heatmaps.Gradient
import com.google.maps.android.heatmaps.HeatmapTileProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.data.repository.VerifiedInferenceRepository
import my.id.jeremia.potholetracker.ui.HomeFind.NavigationActivity.NavigationActivity
import my.id.jeremia.potholetracker.ui.base.BaseViewModel
import my.id.jeremia.potholetracker.ui.common.loader.Loader
import my.id.jeremia.potholetracker.ui.common.snackbar.Messenger
import my.id.jeremia.potholetracker.ui.navigation.Navigator
import my.id.jeremia.potholetracker.utils.common.checkPermission
import my.id.jeremia.potholetracker.utils.googlemaps.InferencePoint
import javax.inject.Inject


@OptIn(FlowPreview::class)
@HiltViewModel()
class HomeFindViewModel @Inject constructor(
    @ApplicationContext val ctx: Context,
    val navigator: Navigator,
    val messenger: Messenger,
    val loader: Loader,
    private val verifiedInferenceRepository: VerifiedInferenceRepository,
) : BaseViewModel(
    loader,
    messenger,
    navigator,
) {

    companion object {
        const val TAG = "HomeFindViewModel"
        val siantarCamPos = LatLng(2.964283004846631, 99.0595995064405)
    }

//    private val _clusterItem = mutableStateListOf<InferencePoint>()
//    val clusterItem : SnapshotStateList<InferencePoint> = _clusterItem

    private val _latlngs = mutableStateListOf<LatLng>()
    val latlngs : SnapshotStateList<LatLng> = _latlngs

    val placesClient = Places.createClient(ctx)
    val placeField = listOf(
        Place.Field.ID,
        Place.Field.NAME, Place.Field.LAT_LNG
    )

    val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    val _searchTextInput = MutableStateFlow<String>("")
    val searchTextInput = _searchTextInput.asStateFlow()

    private val _locationPermission = mutableStateOf(false)
    val locationPermission = _locationPermission

    val _searchResults = mutableStateOf<List<AutocompletePrediction>>(emptyList())
    val searchResult: State<List<AutocompletePrediction>> = _searchResults


    private var _heatmapProvider = mutableStateOf<HeatmapTileProvider?>(null)
    var heatmapProvider : State<HeatmapTileProvider?> = _heatmapProvider

    fun updateSearchingState(value: Boolean) {
        _isSearching.tryEmit(value)
    }

    init {
        updatePermissionState()

        viewModelScope.launch {
            val verifiedInference = verifiedInferenceRepository
                .fetchAll()
                .catch {
                    messenger.deliverRes(Message.error(R.string.something_went_wrong))
                }
                .first()

            if (verifiedInference.isNotEmpty()) {

                for (inference in verifiedInference) {

                    if (inference.status == "berlubang") {
                        // Hanya Berlubang
//                        _clusterItem.add(InferencePoint(LatLng(inference.latitude.toDouble(), inference.longitude.toDouble())))

                        _latlngs.add(
                            LatLng(
                                inference.latitude.toDouble(),
                                inference.longitude.toDouble()
                            ),
                        )
                    }


                }


                val colors = intArrayOf(
                    android.graphics.Color.TRANSPARENT,  // transparent
                    android.graphics.Color.rgb(255, 0, 0) // red
                )
                val startPoints = floatArrayOf(0.8f, 1f)
                val gradient = Gradient(colors, startPoints)

                _heatmapProvider.value = HeatmapTileProvider
                    .Builder()
                    .data(latlngs)
                    .gradient(gradient)
                    .radius(25)
                    .opacity(1.0)
                    .maxIntensity(10.0)
                    .build()
            }
        }

        viewModelScope.launch{
            _searchTextInput.debounce(1000).collectLatest {
                getAutocompleteData(it)
            }
        }

    }

    fun updatePermissionState() {
        _locationPermission.value = checkPermission(
            ctx,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }


    fun onSearchTextInputChange(text: String) {
        _searchTextInput.tryEmit(text);
    }


    fun getAutocompleteData(q: String) {
        val autoCompletePlaceRequest = FindAutocompletePredictionsRequest
            .builder()
            .setQuery(q)
//            .setLocationBias(bias)
            .setRegionCode("ID")
            .build()

        placesClient
            .findAutocompletePredictions(autoCompletePlaceRequest)
            .addOnSuccessListener { resp ->
                _searchResults.value = resp.autocompletePredictions
            }
            .addOnFailureListener {
                Log.e(TAG, it.message.toString())
            }
    }

    fun onShowDirection(ctx: Context, placeId: String) {
        ctx.startActivity(
            Intent(ctx, NavigationActivity::class.java).apply {
                this.putExtra("placeId", placeId)
            }
        )
    }


}