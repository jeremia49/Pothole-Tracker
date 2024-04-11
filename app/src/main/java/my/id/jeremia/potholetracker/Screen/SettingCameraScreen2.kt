package my.id.jeremia.potholetracker.Screen

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.ViewModel.SettingCameraViewModel
import my.id.jeremia.potholetracker.dataStore
import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme


@Composable
fun SettingCameraScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val viewModel: SettingCameraViewModel = viewModel()
    val ctx = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val leftKey = intPreferencesKey("left_rect")
    val topKey = intPreferencesKey("top_rect")
    val widthKey = intPreferencesKey("width_rect")
    val heightKey = intPreferencesKey("height_rect")

    BackHandler {
        (ctx as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onBackPressed()
    }

    LaunchedEffect(Unit) {
        if (viewModel.bitmapImage.value == null)
            (ctx as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    viewModel.setOrientation(LocalConfiguration.current.orientation)

    LaunchedEffect(viewModel.orientation) {
        if (viewModel.orientation.value == Configuration.ORIENTATION_LANDSCAPE) {

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            viewModel.setPreviewView(
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FIT_CENTER
                }
            )

            cameraProviderFuture.addListener({
                // Used to bind the lifecycle of cameras to the lifecycle owner
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                val preview = androidx.camera.core.Preview
                    .Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewModel.previewView.value!!.surfaceProvider)
                    }

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview
                    )

                } catch (exc: Exception) {
                    Log.e("InitCamera", "Use case binding failed", exc)
                }

            }, ContextCompat.getMainExecutor(ctx))
        }
    }

    if (viewModel.bitmapImage.value != null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp)

        )
        {

            AndroidView(
                factory = {
                    CropImageView(it).also {
                        it.setImageBitmap(viewModel.bitmapImage.value!!)
                        it.cropRect = it.wholeImageRect

                        viewModel.setCropRect(it.wholeImageRect!!)

                        it.setOnCropWindowChangedListener {
                            println(it.cropRect)
                            it.cropRect?.let {
                                it1 -> viewModel.setCropRect(it1)
                            }
                        }

                    }
                },
                update={
                    it.setImageBitmap(viewModel.bitmapImage.value!!)
                }
            )

            Button(onClick = {


//                val bmp = viewModel.bitmapImage.value!!

                CoroutineScope(Dispatchers.IO).launch{
                    ctx.dataStore.edit { settings ->
                        settings[leftKey] = viewModel.cropRect.value!!.left
                        settings[topKey] = viewModel.cropRect.value!!.top
                        settings[widthKey] = viewModel.cropRect.value!!.width()
                        settings[heightKey] = viewModel.cropRect.value!!.height()
                    }

                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(ctx,"Berhasil menyimpan pengaturan",Toast.LENGTH_SHORT).show()
                    }

                }

                onBackPressed()

            }) {
                Text("Simpan Pengaturan")
            }


        }


    } else {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {

            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center,
            ) {

                if (viewModel.previewView.value !== null) {
                    AndroidView(
                        factory = { viewModel.previewView.value!! },
                        modifier = Modifier
                            .defaultMinSize(100.dp, 100.dp)
                    )
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {

                Button(onClick = {
                    viewModel.setBitmapImage(
                        viewModel.previewView.value!!.bitmap!!
                    )

                    Toast.makeText(ctx, "Silahkan set area yang diinginkan", Toast.LENGTH_SHORT)
                        .show()

                    (ctx as? Activity)?.requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }) {
                    Text("Ambil Gambar")
                }

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingCameraPreview() {
    PotholeTrackerTheme {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .safeContentPadding()
        ) {
            SettingCameraScreen({})
        }
    }
}
