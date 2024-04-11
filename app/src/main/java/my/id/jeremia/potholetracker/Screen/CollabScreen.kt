package my.id.jeremia.potholetracker.Screen

import android.Manifest
import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import my.id.jeremia.potholetracker.Extension.dashedBorder
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.dataStore
import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme

@Composable
fun CollabScreen(
    onClickSetting: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val leftRect = remember {
        mutableIntStateOf(0)
    }
    val topRect = remember {
        mutableIntStateOf(0)
    }
    val widthRect = remember {
        mutableIntStateOf(0)
    }
    val heightRect = remember {
        mutableIntStateOf(0)
    }

    val scope = rememberCoroutineScope()

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
                    delay(1000L)
                    cameraPermissionRequest.launch(Manifest.permission.CAMERA)
                }

            }
        }


        val leftKey = intPreferencesKey("left_rect")
        val topKey = intPreferencesKey("top_rect")
        val widthKey = intPreferencesKey("width_rect")
        val heightKey = intPreferencesKey("height_rect")

        val leftRectFlow: Flow<Int> = context.dataStore.data.map {
            it[leftKey] ?: 0
        }
        val rightRectFlow: Flow<Int> = context.dataStore.data.map {
            it[topKey] ?: 0
        }
        val widthRectFlow: Flow<Int> = context.dataStore.data.map {
            it[widthKey] ?: 0
        }
        val heightRectFlow: Flow<Int> = context.dataStore.data.map {
            it[heightKey] ?: 0
        }

        scope.launch {
            leftRectFlow.collect {
                leftRect.value = it
            }
        }

        scope.launch {
            rightRectFlow.collect {
                topRect.value = it
            }
        }

        scope.launch {
            widthRectFlow.collect {
                widthRect.value = it
            }
        }

        scope.launch {
            heightRectFlow.collect {
                heightRect.value = it
            }
        }


    }

    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = it)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            if (isCameraAccepted.value) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                ) {


                    IconButton(
                        onClick = {
                            onClickSetting()
                        },
                        modifier = modifier
                            .width(35.dp)
                            .align(Alignment.End)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.baseline_settings_24),
                            "Setting Camera",
                            modifier = modifier
                                .fillMaxSize()
                        )
                    }




                    Text(
                        "Rect :\n" +
                                "Left : ${leftRect.value}\n" +
                                "Top : ${topRect.value}\n" +
                                "Width : ${widthRect.value}\n" +
                                "Height : ${heightRect.value}\n"
                    )


//                    AndroidView(
//                        factory = { viewModel.previewView.value!! },
//                        modifier = Modifier
//                            .defaultMinSize(100.dp, 100.dp)
//                            .border(1.dp, androidx.compose.ui.graphics.Color.Red),
//                    )

                }

            } else {
                Text(
                    text = "Silahkan perbolehkan akses kamera\nuntuk mengambil gambar.",
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
            CollabScreen({})
        }
    }
}