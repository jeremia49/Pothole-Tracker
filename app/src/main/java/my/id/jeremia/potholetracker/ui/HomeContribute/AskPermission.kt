package my.id.jeremia.potholetracker.ui.HomeContribute

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.id.jeremia.potholetracker.utils.common.askPermission
import my.id.jeremia.potholetracker.utils.common.checkPermission

@Composable
fun AskPermissionView(
    modifier: Modifier,
    viewModel: HomeContributeViewModel
) {

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        viewModel.updatePermissionState();
    }

    if (!viewModel.cameraPermission.value) {
        Text(
            text = "Anda belum memperbolehkan akses kamera",
            fontSize = 15.sp,
            modifier = Modifier,
        )
        ElevatedButton(
            onClick = {
                askPermission(
                    arrayOf(Manifest.permission.CAMERA),
                    launcherMultiplePermissions
                )
            },
            modifier = Modifier
                .padding(bottom=20.dp)
        ) {
            Text("Perbolehkan akses kamera")
        }
    }


    if (!viewModel.locationPermission.value) {
        Text(
            text = "Anda belum memperbolehkan akses lokasi",
            fontSize = 15.sp,
            modifier = Modifier
        )
        ElevatedButton(
            onClick = {
                askPermission(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    ),
                    launcherMultiplePermissions
                )
            },
            modifier = Modifier
        ) {
            Text("Perbolehkan akses lokasi")
        }
    }


}