package my.id.jeremia.potholetracker.Screen

import android.Manifest
import android.R.attr.bitmap
import android.content.pm.PackageManager
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.withContext
import my.id.jeremia.potholetracker.Data.InferenceData
import my.id.jeremia.potholetracker.Extension.toBitmap
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.Requests.LocationRequest
import my.id.jeremia.potholetracker.Tensorflow.RescaleOp
import my.id.jeremia.potholetracker.Tensorflow.TFlite
import my.id.jeremia.potholetracker.ViewModel.CollabViewModel
import my.id.jeremia.potholetracker.dataStore
import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme
import my.id.jeremia.potholetracker.utils.saveBitmapToFile
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.task.gms.vision.classifier.Classifications
import kotlin.concurrent.fixedRateTimer
import kotlin.math.floor


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
    val isCameraViewEnabled = remember {
        mutableStateOf(true)
    }
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
    val isAllPermissionAccepted = remember {
        mutableStateOf(false)
    }
    val isSettingCamera = remember {
        mutableStateOf(false)
    }
    val isRectValid = remember {
        mutableStateOf(false)
    }
    val lastInferenceResult = remember {
        mutableStateOf("None")
    }
    val isTakingImage = remember {
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

    val requestAllPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        var allPermissionAccepted = true
        for (perm in permissions) {
            if (!perm.value) {
                allPermissionAccepted = false
            }
        }

        if (allPermissionAccepted) {
            isAllPermissionAccepted.value = true
            LocationRequest(context).requestLocationUpdate(viewModel)
        }

    }


    LaunchedEffect(Unit) {

        var locationPerm = false
        var cameraPerm = false

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            cameraPerm = true
        }
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPerm = true
        }

        if (locationPerm && cameraPerm) {
            isAllPermissionAccepted.value = true
            LocationRequest(context).requestLocationUpdate(viewModel)
        }

        if (!isAllPermissionAccepted.value) {
            scope.launch {
                delay(2000L)
                requestAllPermission.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
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

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FIT_CENTER
        }
    }

    LaunchedEffect(isAllPermissionAccepted, viewModel.orientation) {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                val preview = androidx.camera.core.Preview
                    .Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, imageCapture, preview,
                )

                if (viewModel.timer.value != null) {
                    viewModel.cancelTimer()
                }

                val timer = fixedRateTimer("capture", false, 0, 2000L) {

                    Handler(Looper.getMainLooper()).post {
                        isTakingImage.value = true
                        imageCapture.takePicture(
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageCapturedCallback() {
                                override fun onError(exc: ImageCaptureException) {
                                    Log.e(
                                        "ImageCapture",
                                        "Photo capture failed: ${exc.message}",
                                        exc
                                    )
                                    isTakingImage.value = false
                                }

                                override fun onCaptureSuccess(image: ImageProxy) {
                                    super.onCaptureSuccess(image)
                                    isTakingImage.value = false

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

                                        if (viewModel.isInferenceStarted.value) {
                                            val savedFile = saveBitmapToFile(context, croppedBitmap)

                                            val imageProcessor: ImageProcessor =
                                                ImageProcessor.Builder()
                                                    .add(
                                                        ResizeWithCropOrPadOp(448, 448),
                                                    )
                                                    .add(
                                                        RescaleOp(),
                                                    )
                                                    .build()

                                            val timage = TensorImage(DataType.FLOAT32)
                                            timage.load(croppedBitmap)

                                            val processedTImage = imageProcessor.process(timage);

                                            var results: MutableList<Classifications>? = null;

                                            if (TFlite.imageClassifier != null) {
                                                var confidence = 0f
                                                var status = ""
                                                try {
                                                    results = TFlite.imageClassifier!!.classify(
                                                        processedTImage
                                                    )
                                                    println(results)
                                                    lastInferenceResult.value =
                                                        results!![0].categories[0].label + " (" + results!![0].categories[0].score + ")"
                                                    status =
                                                        results?.get(0)?.categories?.get(0)?.label!!.trim()
                                                    confidence = results!![0].categories[0].score
                                                } catch (e: Exception) {
                                                    lastInferenceResult.value =
                                                        "Model Error ! ${e.message}"
                                                    Log.e("INFERENCE", "${e.message}")
                                                }

                                                viewModel.addInference(
                                                    inferenceData = InferenceData(
                                                        viewModel.locationData.value!!.latitude,
                                                        viewModel.locationData.value!!.longitude,
                                                        savedFile!!.absolutePath,
                                                        status,
                                                        confidence
                                                    )
                                                )

                                            }

                                        }


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

            if (isAllPermissionAccepted.value) {

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

                            Column(
                                modifier = modifier
                                ) {

                                Row(
                                    modifier = modifier,
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Switch(
                                        checked = isCameraViewEnabled.value,
                                        onCheckedChange = {
                                            isCameraViewEnabled.value = !isCameraViewEnabled.value
                                        }
                                    )
                                    Text("Camera Preview")
                                }


                                Box(
                                    contentAlignment = Alignment.Center,
                                ) {

                                    AndroidView(
                                        factory = { previewView },
                                        modifier = Modifier
                                            .defaultMinSize(100.dp, 100.dp)
                                            .align(Alignment.Center),
                                        update = {
                                            if (isCameraViewEnabled.value) {
                                                it.visibility = View.VISIBLE
                                            } else {
                                                it.visibility = View.INVISIBLE
                                            }
                                        }
                                    )


                                    if (!isCameraViewEnabled.value) {
                                        if (viewModel.bitmapImage.value != null) {
                                            Image(
                                                viewModel.bitmapImage.value!!.asImageBitmap(),
                                                "Current Image",
                                                modifier = Modifier
                                                    .requiredWidthIn(min=250.dp)
                                                    .requiredHeightIn(min=250.dp)
                                                    .align(Alignment.Center)
                                            )
                                        } else {
                                            CircularProgressIndicator(
                                                modifier = Modifier
                                                    .align(Alignment.Center)
                                            )
                                        }
                                    }
                                }


                            }


                        }


                        Column(
                            modifier = modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Row(
                                    modifier = modifier,
                                    horizontalArrangement = Arrangement.Start,
                                ) {


                                    IconButton(
                                        onClick = {},
                                    ) {
                                        Icon(
                                            painterResource(id = R.drawable.baseline_camera_24),
                                            "Taking image",
                                            modifier = modifier
                                                .fillMaxSize(),
                                            tint = if (isTakingImage.value) Color.Transparent else if (viewModel.isInferenceStarted.value) Color.Red else MaterialTheme.colorScheme.outline
                                        )
                                    }

                                    IconButton(
                                        onClick = {},
                                    ) {
                                        Icon(
                                            painterResource(id = R.drawable.baseline_my_location_24),
                                            "Location Update",
                                            modifier = modifier
                                                .fillMaxSize(),
                                            tint =
                                            if (!viewModel.gotLocationUpdate.value) Color.Transparent else if (viewModel.isInferenceStarted.value) Color.Red else MaterialTheme.colorScheme.outline
                                        )
                                    }

                                    if (viewModel.gotLocationUpdate.value) {
                                        LaunchedEffect(Unit) {
                                            scope.launch {
                                                delay(500)
                                                viewModel.setGotLocationUpdate(false)
                                            }
                                        }
                                    }

                                }


                                Row(
                                    modifier = modifier,
                                    horizontalArrangement = Arrangement.End,
                                ) {

                                    if (viewModel.isInferenceStarted.value) {
                                        IconButton(
                                            onClick = {

                                                viewModel.stopInference()

                                                Handler(Looper.getMainLooper()).post {
                                                    Toast.makeText(
                                                        context,
                                                        "Inference dihentikan",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }

                                            }) {
                                            Icon(
                                                painterResource(id = R.drawable.baseline_stop_24),
                                                "Start",
                                                modifier = modifier
                                                    .fillMaxSize(),
                                                tint = Color.Red,
                                            )
                                        }
                                    } else {
                                        IconButton(
                                            enabled =
                                            (viewModel.bitmapImage.value != null && viewModel.locationData.value != null),
                                            onClick = {

                                                viewModel.startInference()

                                                Handler(Looper.getMainLooper()).post {
                                                    Toast.makeText(
                                                        context,
                                                        "Inference dimulai",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }

                                            }) {
                                            Icon(
                                                painterResource(id = R.drawable.baseline_play_arrow_24),
                                                "Start",
                                                modifier = modifier
                                                    .fillMaxSize(),
                                                tint = if (viewModel.bitmapImage.value != null && viewModel.locationData.value != null) Color.Green else Color.Gray,
                                            )
                                        }
                                    }



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
                                            if(viewModel.originalBitmapImage.value==null){
                                                Toast.makeText(
                                                    context,
                                                    "Silahkan tunggu hingga minimal 1x capture",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                return@IconButton;
                                            }

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


                            }

                            Text(
                                "Last Result : \n${lastInferenceResult.value} \n",
                                textAlign = TextAlign.Center,
                                modifier = modifier
                                    .fillMaxWidth(),
                            )

                            Text(
                                "Location : \n${
                                    if (viewModel.locationData.value == null) "Tidak tersedia\n"
                                    else "Latitude : ${viewModel.locationData.value!!.latitude}\n" +
                                            "Longitude : ${viewModel.locationData.value!!.longitude}\n" +
                                            "Speed : ${floor(viewModel.locationData.value!!.speed * 3.6)} km/h\n" +
                                            "Accuracy : ${viewModel.locationData.value!!.accuracy}\n" +
                                            "Speed Accuracy : ${viewModel.locationData.value!!.speedAccuracy}\n"
                                }"
                            )

                            Text(
                                "Rect :\n" +
                                        "Left : ${leftRect.intValue}\n" +
                                        "Top : ${topRect.intValue}\n" +
                                        "Width : ${widthRect.intValue}\n" +
                                        "Height : ${heightRect.intValue}\n" +
                                        "Valid : ${isRectValid.value}\n"

                            )


                        }

                    }


                }


            } else {
                Text(
                    text = "Silahkan perbolehkan akses kamera dan lokasi untuk lanjut ke tahap berikutnya",
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