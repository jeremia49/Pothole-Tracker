package my.id.jeremia.potholetracker.ui.HomeContribute

import android.Manifest
import android.content.Intent
import android.location.LocationRequest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.rememberPermissionState
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.ui.common.image.NetworkImage
import my.id.jeremia.potholetracker.utils.common.askPermission
import my.id.jeremia.potholetracker.utils.common.checkPermission
import my.id.jeremia.potholetracker.utils.crypto.hashSHA256

@Composable
fun HomeContribute(modifier: Modifier = Modifier, viewModel: HomeContributeViewModel) {
    BackHandler { viewModel.navigator.finish() }
    HomeContributeView(
        modifier,
        viewModel
    )
}


@Composable
fun HomeContributeView(
    modifier: Modifier = Modifier,
    viewModel: HomeContributeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!viewModel.cameraPermission.value || !viewModel.locationPermission.value) {
                AskPermissionView(modifier = Modifier, viewModel)
            }else{
                OutlinedButton(onClick = {
                    context.startActivity(Intent(context, ContributeActivity::class.java))
                }) {
                    Text("Mulai Kontribusi")
                }
            }

        }
    }


}

@Preview(showBackground = true)
@Composable
private fun HomeContributePreview() {
    HomeContributeView(
    )
}