package my.id.jeremia.potholetracker.ui.HomeFind

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.id.jeremia.potholetracker.utils.common.askPermission

@Composable
fun askPermissionView(
    updateState : ()->Unit,
){
    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        updateState();
    }

    Text(
        text = "Untuk menggunakan Maps,\nperbolehkan akses lokasi !",
        fontSize = 15.sp,
        modifier = Modifier,
        textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.height(15.dp))
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
