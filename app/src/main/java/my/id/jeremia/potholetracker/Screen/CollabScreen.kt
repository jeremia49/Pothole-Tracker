package my.id.jeremia.potholetracker.Screen

import android.Manifest
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalConfiguration
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
import my.id.jeremia.potholetracker.Extension.toBitmap
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.ViewModel.CollabViewModel
import my.id.jeremia.potholetracker.dataStore
import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme
import java.util.concurrent.Executors
import kotlin.concurrent.fixedRateTimer


@Composable
@androidx.annotation.OptIn(
    androidx.camera.core.ExperimentalGetImage::class,
    androidx.camera.core.ExperimentalZeroShutterLag::class
)
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
    val isRectValid = remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current


    BackHandler {
        if (viewModel.timer.value !== null) {
            viewModel.cancelTimer()
        }
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

    viewModel.setOrientation(LocalConfiguration.current.orientation)

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()

    }


    LaunchedEffect(isCameraAccepted, viewModel.orientation) {

        if (!isCameraAccepted.value) return@LaunchedEffect

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA


            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, imageCapture,
                )

                if (viewModel.timer.value != null) {
                    viewModel.cancelTimer()
                }
                val timer = fixedRateTimer("preview", false, 0, 1000L) {

                    Handler(Looper.getMainLooper()).post {

                        imageCapture.takePicture(
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageCapturedCallback() {
                                override fun onError(exc: ImageCaptureException) {
                                    Log.e(
                                        "ImageCapture",
                                        "Photo capture failed: ${exc.message}",
                                        exc
                                    )
                                }

                                override fun onCaptureSuccess(image: ImageProxy) {
                                    super.onCaptureSuccess(image)

                                    CoroutineScope(Dispatchers.Default).launch {

                                        val originalbitmap = image.image!!.toBitmap()
                                        viewModel.setOriginalBitmapImage(originalbitmap)

                                        val croppedBitmap: Bitmap
                                        if (widthRect.intValue == 0 || heightRect.intValue == 0) {
                                            isRectValid.value = false
                                            croppedBitmap =
                                                originalbitmap.copy(originalbitmap.config, true)
                                        } else if ((originalbitmap.width < leftRect.intValue + widthRect.intValue) || (originalbitmap.height < topRect.intValue + heightRect.intValue)) {
                                            isRectValid.value = false
                                            croppedBitmap =
                                                originalbitmap.copy(originalbitmap.config, false)
                                        } else {
                                            isRectValid.value = true
                                            croppedBitmap = Bitmap.createBitmap(
                                                originalbitmap,
                                                leftRect.intValue,
                                                topRect.intValue,
                                                widthRect.intValue,
                                                heightRect.intValue,
                                            )
                                        }

                                        viewModel.setBitmapImage(croppedBitmap)
                                        image.close()

                                    }


                                }
                            }

                        )

                    }

                }
                viewModel.setTimer(timer)

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

                    AutomaticRowColumn(
                        modifier = modifier
                            .fillMaxSize(),
                        viewModel.orientation.value,
                    ) {
                        AndroidView(
                            factory = {
                                CropImageView(it).also {
                                    it.setImageBitmap(viewModel.originalBitmapImage.value!!)
                                    it.cropRect = it.wholeImageRect

                                    viewModel.setCropRect(it.wholeImageRect!!)

                                    it.setOnCropWindowChangedListener {
//                                        println(it.cropRect)
                                        it.cropRect?.let { it1 ->
                                            viewModel.setCropRect(it1)
                                        }
                                    }

                                }
                            },
                        )


                        Button(onClick = {

                            if (viewModel.timer.value !== null) {
                                viewModel.cancelTimer()
                            }

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


                                    onBackPressed()

                                }


                            }

                            isSettingCamera.value = false

                        }) {
                            Text("Simpan Pengaturan")
                        }

                    }

                } else {

                    AutomaticRowColumn(
                        modifier = modifier
                            .fillMaxSize(),
                        viewModel.orientation.value
                    ) {

                        Box(
                            modifier = modifier
                                .defaultMinSize(100.dp, 100.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            if (viewModel.bitmapImage.value != null) {
                                Image(
                                    viewModel.bitmapImage.value!!.asImageBitmap(),
                                    "Current Image",
                                    modifier = modifier
                                        .fillMaxWidth(0.7f)
                                        .fillMaxHeight(0.7f),
                                )
                            } else {
                                CircularProgressIndicator()
                            }

                        }

                        Column(
                            modifier = modifier
                                .fillMaxSize()
                        ) {

                            Row(
                                modifier = modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                            ) {


                                IconButton(onClick = {

                                    if (viewModel.timer.value !== null) {
                                        viewModel.cancelTimer()
                                    }

                                    val leftKey = intPreferencesKey("left_rect")
                                    val topKey = intPreferencesKey("top_rect")
                                    val widthKey = intPreferencesKey("width_rect")
                                    val heightKey = intPreferencesKey("height_rect")

                                    CoroutineScope(Dispatchers.IO).launch {
                                        context.dataStore.edit { settings ->
                                            settings[leftKey] = 0
                                            settings[topKey] = 0
                                            settings[widthKey] = 0
                                            settings[heightKey] = 0
                                        }

                                        Handler(Looper.getMainLooper()).post {
                                            Toast.makeText(
                                                context,
                                                "Berhasil mereset pengaturan",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            onBackPressed()

                                        }


                                    }

                                }) {
                                    Icon(
                                        painterResource(id = R.drawable.baseline_delete_24),
                                        "Reset Camera",
                                        modifier = modifier
                                            .fillMaxSize()
                                    )
                                }


                                IconButton(
                                    onClick = {

                                        if (viewModel.timer.value !== null) {
                                            viewModel.cancelTimer()
                                        }

                                        isSettingCamera.value = true
                                    },
                                    modifier = modifier
                                        .width(35.dp)
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.baseline_settings_24),
                                        "Setting Camera",
                                        modifier = modifier
                                            .fillMaxSize()
                                    )
                                }


                            }


                            Text(
                                "Rect :\n" +
                                        "Left : ${leftRect.intValue}\n" +
                                        "Top : ${topRect.intValue}\n" +
                                        "Width : ${widthRect.intValue}\n" +
                                        "Height : ${heightRect.intValue}\n" +
                                        "Valid : ${isRectValid.value}\n"

                            )

//                            Button(onClick = {
//
//
//                            }) {
//                                Text("Process")
//                            }

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

@Composable
fun AutomaticRowColumn(
    modifier: Modifier = Modifier,
    orientation: Int,
    content: @Composable () -> Unit
) {
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        return Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            content()
        }
    }
    return Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        content()
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