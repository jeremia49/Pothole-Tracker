package my.id.jeremia.potholetracker.Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import my.id.jeremia.potholetracker.ViewModel.MapViewModel
import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme

@Composable
fun MapScreen(modifier: Modifier = Modifier) {
    val viewModel: MapViewModel = viewModel()

    val inferenceList = viewModel.getAllInferences.collectAsState(initial = listOf())

    Scaffold(
        modifier = modifier
            .fillMaxSize(),

        ) {
        Box(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {

            val simalungun = LatLng(2.964283004846631, 99.0595995064405)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(simalungun, 9f)
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {

                repeat(inferenceList.value.size) {
                    Marker(
                        state = MarkerState(
                            position =
                            LatLng(
                                inferenceList.value[it].latitude,
                                inferenceList.value[it].longitude,
                            ),
                        ),
                        title = "Berlubang",
                    )

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapPreview() {
    PotholeTrackerTheme {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .safeContentPadding()
        ) {
            MapScreen()
        }
    }
}
