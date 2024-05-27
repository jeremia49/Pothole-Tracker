package my.id.jeremia.potholetracker.ui.HomeFind


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.data.local.db.entity.VerifiedInference
import my.id.jeremia.potholetracker.ui.HomeMyAccount.HomeMyAccountViewModel
import my.id.jeremia.potholetracker.ui.theme.grey
import my.id.jeremia.potholetracker.ui.theme.manropeFontFamily


@Composable
fun HomeFind(modifier: Modifier = Modifier, viewModel: HomeFindViewModel) {
    BackHandler { viewModel.navigator.finish() }
    HomeFindView(
        modifier,
        inferences = viewModel.inferences,
    )
}


@Composable
fun HomeFindView(
    modifier: Modifier = Modifier,
    inferences: List<VerifiedInference> = emptyList(),
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {

        val siantar = LatLng(2.964283004846631,  99.0595995064405)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(siantar, 10f)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            repeat(inferences.size){
                val marker = LatLng(inferences[it].latitude.toDouble(),  inferences[it].longitude.toDouble())
                Marker(
                    state = MarkerState(position = marker),
                )
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