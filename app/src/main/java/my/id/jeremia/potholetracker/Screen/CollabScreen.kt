package my.id.jeremia.potholetracker.Screen

import android.Manifest
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canhub.cropper.CropImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.ViewModel.CollabViewModel
import my.id.jeremia.potholetracker.dataStore
import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme
import kotlin.concurrent.fixedRateTimer

@Composable
@androidx.annotation.OptIn(androidx.camera.view.TransformExperimental::class)
fun CollabScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: CollabViewModel = viewModel()

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
    val isCameraAccepted = remember {
        mutableStateOf(false)
    }
    val isSettingCamera = remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current


    BackHandler {
        (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onBackPressed()
    }

    val cameraPermissionRequest =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            isCameraAccepted.value = it
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
                leftRect.intValue = it
            }
        }

        scope.launch {
            rightRectFlow.collect {
                topRect.intValue = it
            }
        }

        scope.launch {
            widthRectFlow.collect {
                widthRect.intValue = it
            }
        }

        scope.launch {
            heightRectFlow.collect {
                heightRect.intValue = it
            }
        }


    }

    LaunchedEffect(Unit) {
        if (viewModel.bitmapImage.value == null)
            (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }


    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FIT_CENTER
            visibility = View.INVISIBLE
        }
    }

    LaunchedEffect(Unit) {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val preview = androidx.camera.core.Preview
                .Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview
                )


                fixedRateTimer("preview", false, 0, 1000L) {

                    Handler(Looper.getMainLooper()).post {
                        val originalbitmap = previewView.bitmap ?: return@post

                        viewModel.setOriginalBitmapImage(originalbitmap)

                        val croppedBitmap:Bitmap = if((widthRect.intValue == 0)&&(heightRect.intValue==0)){
                            originalbitmap
                        }else{
                            Bitmap.createBitmap(
                                originalbitmap,
                                leftRect.intValue,
                                topRect.intValue,
                                widthRect.intValue,
                                heightRect.intValue,
                            )
                        }


                        viewModel.setBitmapImage(croppedBitmap)
                    }

                }

            } catch (exc: Exception) {
                Log.e("InitCamera", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))

    }



    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = it)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            if (isCameraAccepted.value) {

                if (isSettingCamera.value) {

                    Row(
                        modifier = modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AndroidView(
                            factory = {
                                CropImageView(it).also {
                                    it.setImageBitmap(viewModel.originalBitmapImage.value!!)
                                    it.cropRect = it.wholeImageRect

                                    viewModel.setCropRect(it.wholeImageRect!!)

                                    it.setOnCropWindowChangedListener {
                                        println(it.cropRect)
                                        it.cropRect?.let { it1 ->
                                            viewModel.setCropRect(it1)
                                        }
                                    }

                                }
                            },
                        )


                        Button(onClick = {

                            val leftKey = intPreferencesKey("left_rect")
                            val topKey = intPreferencesKey("top_rect")
                            val widthKey = intPreferencesKey("width_rect")
                            val heightKey = intPreferencesKey("height_rect")

                            CoroutineScope(Dispatchers.IO).launch {
                                context.dataStore.edit { settings ->
                                    settings[leftKey] = viewModel.cropRect.value!!.left
                                    settings[topKey] = viewModel.cropRect.value!!.top
                                    settings[widthKey] = viewModel.cropRect.value!!.width()
                                    settings[heightKey] = viewModel.cropRect.value!!.height()
                                }

                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(
                                        context,
                                        "Berhasil menyimpan pengaturan",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                                    onBackPressed()

                                }



                            }

                            isSettingCamera.value = false

                        }) {
                            Text("Simpan Pengaturan")
                        }

                    }

                } else {

                    Row(
                        modifier = modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            AndroidView(
                                factory = { previewView },
                                modifier = Modifier
                                    .defaultMinSize(100.dp, 100.dp)
                                    .border(1.dp, androidx.compose.ui.graphics.Color.Red)
                            )

                            if (viewModel.bitmapImage.value != null) {
                                Image(
                                    viewModel.bitmapImage.value!!.asImageBitmap(),
                                    "Current Image"
                                )
                            }
                        }

                        Column(
                            modifier = modifier
                                .fillMaxSize()
                        ) {


                            IconButton(
                                onClick = {
                                    isSettingCamera.value = true
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
                                        "Left : ${leftRect.intValue}\n" +
                                        "Top : ${topRect.intValue}\n" +
                                        "Width : ${widthRect.intValue}\n" +
                                        "Height : ${heightRect.intValue}\n"
                            )

                        }

                    }


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