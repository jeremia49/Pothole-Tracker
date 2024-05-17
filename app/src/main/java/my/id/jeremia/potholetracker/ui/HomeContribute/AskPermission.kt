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
import my.id.jeremia.potholetracker.utils.common.askPermission
import my.id.jeremia.potholetracker.utils.common.checkPermission

@Composable
fun askPermissionView(
    modifier: Modifier,
    context: Context,
) {

    val cameraPermission = remember() {
        mutableStateOf(
            checkPermission(
                context, arrayOf(Manifest.permission.CAMERA)
            )
        )
    }

    val locationPermission = remember() {
        mutableStateOf(
            checkPermission(
                context,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        )
    }

    println(cameraPermission.value)
    println(locationPermission.value)

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        cameraPermission.value = checkPermission(
            context, arrayOf(Manifest.permission.CAMERA)
        )

        locationPermission.value = checkPermission(
            context,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    if (!cameraPermission.value || !locationPermission.value) {
        Text(
            "Camera Permission : ${cameraPermission.value}",
        )
        if (!cameraPermission.value) {
            ElevatedButton(
                onClick = {
                    askPermission(
                        arrayOf(Manifest.permission.CAMERA),
                        launcherMultiplePermissions
                    )
                },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text("Perbolehkan akses kamera")
            }
        }

        Text(
            "Location Permission : ${locationPermission.value}",
        )
        if (!locationPermission.value) {
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
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text("Perbolehkan akses lokasi")
            }
        }

    }


}