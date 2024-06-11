package my.id.jeremia.potholetracker.ui.HomeFind


import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.TileOverlay
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.heatmaps.Gradient
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.maps.android.heatmaps.WeightedLatLng
import my.id.jeremia.potholetracker.data.local.db.entity.VerifiedInference
import my.id.jeremia.potholetracker.utils.googlemaps.InferencePoint


@Composable
fun HomeFind(modifier: Modifier = Modifier, viewModel: HomeFindViewModel) {
    BackHandler { viewModel.navigator.finish() }
    HomeFindView(
        modifier,
        clusterItem = viewModel.clusterItem,
        latlngs = viewModel.latlngs,
        searchTextInput = viewModel.searchTextInput.collectAsStateWithLifecycle().value,
        onSearchTextInputChange = { viewModel.onSearchTextInputChange(it) },
        locationPermission = viewModel.locationPermission.value,
        updateLocationState = { viewModel.updatePermissionState() },
        isSearching = viewModel.isSearching.collectAsStateWithLifecycle().value,
        updateSearchState = { viewModel.updateSearchingState(it) },
        searchResult = viewModel.searchResult.value,
        onShowDirection = viewModel::onShowDirection
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFindView(
    modifier: Modifier = Modifier,
    clusterItem : List<InferencePoint> = emptyList(),
    latlngs: List<WeightedLatLng> = emptyList(),
    searchTextInput: String = "",
    onSearchTextInputChange: (String) -> Unit = {},
    locationPermission: Boolean = false,
    updateLocationState: () -> Unit = {},
    isSearching: Boolean = false,
    updateSearchState: (Boolean) -> Unit = {},
    searchResult: List<AutocompletePrediction> = emptyList(),
    onShowDirection: (Context, String) -> Unit = { context: Context, s: String -> },
) {
    val context = LocalContext.current

    if (!locationPermission) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            askPermissionView(updateLocationState);
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        ) {

            var heatmapprovider: HeatmapTileProvider? = null;
            if (latlngs.isNotEmpty()) {
                val colors = intArrayOf(
                    android.graphics.Color.rgb(0, 255, 0),  // green
                    android.graphics.Color.rgb(255, 0, 0) // red
                )
                val startPoints = floatArrayOf(0.8f, 1f)
                val gradient = Gradient(colors, startPoints)

                heatmapprovider = HeatmapTileProvider
                    .Builder()
                    .weightedData(latlngs)
                    .gradient(gradient)
                    .build()

                heatmapprovider
                    .setOpacity(1.0)
            }

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(HomeFindViewModel.siantarCamPos, 10f)
            }

            GoogleMap(
                modifier = modifier
                    .fillMaxSize(),
                cameraPositionState = cameraPositionState,
                googleMapOptionsFactory = {

                    GoogleMapOptions()
                        .mapToolbarEnabled(true)
                        .compassEnabled(true)
                },
                uiSettings = MapUiSettings(),
                properties = MapProperties(
                    isMyLocationEnabled = true,
                ),
                contentPadding = with(LocalDensity.current) {
                    PaddingValues(
                        top = 100.dp,
                    )
                },
            ) {

                if (heatmapprovider != null) {
                    TileOverlay(tileProvider = heatmapprovider)
                }

                Clustering(items = clusterItem)

            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .systemBarsPadding()
            )
            {

                SearchBar(
                    modifier = Modifier
                        .padding(if (isSearching) 0.dp else 15.dp)
                        .fillMaxWidth()
                    ,
                    query = searchTextInput,
                    onQueryChange = onSearchTextInputChange,
                    onSearch = {},
                    active = isSearching,
                    onActiveChange = updateSearchState,
                    trailingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    placeholder = { Text("Cari tempat tujuan anda...", color = Color.Gray) },
                    colors = SearchBarDefaults.colors(
                        containerColor = Color.White,
                        inputFieldColors = OutlinedTextFieldDefaults.colors().copy(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor = Color.Black,
                            focusedTextColor =  Color.Black,
                        ),
                    ),
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(searchResult.size) { index ->
                            Row(
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 50.dp)
                                    .fillMaxWidth()
                                    .padding(15.dp)
                                    .clickable {
                                        onShowDirection(context, searchResult[index].placeId)
                                    }
                            ) {
                                Icon(Icons.Default.PinDrop, contentDescription = null)
                                Text(
                                    searchResult[index].getFullText(null).toString(),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f),
                                    color = Color.Black,
                                )
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowRightAlt,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }

            }
        }
    }


}

@Preview(showBackground = true)
@Composable
private fun HomeFindPreview() {
    HomeFindView(

    )
}