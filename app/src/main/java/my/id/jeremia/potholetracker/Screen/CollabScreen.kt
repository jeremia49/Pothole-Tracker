package my.id.jeremia.potholetracker.Screen

import android.Manifest
import android.app.Instrumentation.ActivityResult
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.Extension.dashedBorder
import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme

@Composable
fun CollabScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val isCameraAccepted = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val cameraPermissionRequest =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            if (it) {
                isCameraAccepted.value = true
            } else {
                isCameraAccepted.value = false
            }
        }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) -> {
                isCameraAccepted.value = true
            }

            else -> {
                isCameraAccepted.value = false
                scope.launch {
                    snackbarHostState.showSnackbar("Akses kamera diperlukan")
                }
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            if (isCameraAccepted.value) {
                Box(
                    modifier = modifier
                        .padding(20.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ){
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){

                        Text("Berikut tampilan kamera anda : ")

                        Spacer(modifier=modifier
                            .height(10.dp)
                        )

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = modifier
                                .border(1.dp, androidx.compose.ui.graphics.Color.Black)
                        ){
                            CameraPreviewScreen()

                        }


                    }
                }

            } else {
                Text(
                    text = "Akses Kamera diperlukan untuk lanjut ke halaman berikutnya",
                    textAlign = TextAlign.Center,

                    )
            }

        }
    }


}

@Preview(showBackground = true)
@Composable
fun CollabPreview() {
    PotholeTrackerTheme {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .safeContentPadding()
        ) {
            CollabScreen()
        }
    }
}