//package my.id.jeremia.potholetracker.Screen
//
//import android.app.Activity
//import android.content.pm.ActivityInfo
//import android.content.res.Configuration
//import android.graphics.Bitmap
//import android.graphics.Color
//import android.os.Environment
//import android.util.Log
//import android.util.Rational
//import android.view.Surface
//import android.widget.Toast
//import androidx.activity.compose.BackHandler
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.UseCaseGroup
//import androidx.camera.core.ViewPort
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.absoluteOffset
//import androidx.compose.foundation.layout.defaultMinSize
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.safeContentPadding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import androidx.core.view.setPadding
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import kotlinx.coroutines.coroutineScope
//import my.id.jeremia.potholetracker.ViewModel.SettingCameraViewModel
//import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//
//@Composable
//fun SettingCameraScreen2(
//    onBackPressed: () -> Unit,
//    modifier: Modifier = Modifier,
//) {
//
//    val viewModel: SettingCameraViewModel = viewModel()
//    val ctx = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//
//    BackHandler {
//        (ctx as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        onBackPressed()
//    }
//
//    LaunchedEffect(Unit) {
//        (ctx as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//    }
//
//    viewModel.setOrientation(LocalConfiguration.current.orientation)
//
//    LaunchedEffect(viewModel.orientation) {
//        if (viewModel.orientation.value == Configuration.ORIENTATION_LANDSCAPE) {
//
//            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
//
//            viewModel.setPreviewView(
//                PreviewView(ctx).apply {
//                    scaleType = PreviewView.ScaleType.FIT_CENTER
//                    setBackgroundColor(Color.BLACK)
//                    setPadding(1)
//                }
//            )
//
//            cameraProviderFuture.addListener({
//                println("listener camera provider future ")
//                // Used to bind the lifecycle of cameras to the lifecycle owner
//                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//                val preview = androidx.camera.core.Preview
//                    .Builder()
//                    .build()
//                    .also {
//                        it.setSurfaceProvider(viewModel.previewView.value!!.surfaceProvider)
//                    }
//
//                try {
//                    // Unbind use cases before rebinding
//                    cameraProvider.unbindAll()
//
//                    // Bind use cases to camera
//                    cameraProvider.bindToLifecycle(
//                        lifecycleOwner, cameraSelector, preview
//                    )
//
//                } catch (exc: Exception) {
//                    Log.e("InitCamera", "Use case binding failed", exc)
//                }
//
//            }, ContextCompat.getMainExecutor(ctx))
//        }
//    }
//
//
//
//    Row(
//        modifier = Modifier
//            .fillMaxSize(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center,
//    ) {
//
//        Box(
//            modifier = Modifier,
//            contentAlignment = Alignment.Center,
//        ) {
//
//            Text("Hello World ")
//
//            if (viewModel.previewView.value!==null) {
//                AndroidView(
//                    factory = { viewModel.previewView.value!! },
//                    modifier = Modifier
//                        .defaultMinSize(100.dp, 100.dp)
//                        .border(1.dp, androidx.compose.ui.graphics.Color.Red),
//                )
//            }
//
//            Box(
//                modifier =
//                Modifier
//                    .width(viewModel.widthRect.value.dp)
//                    .height(viewModel.heightRect.value.dp)
//                    .absoluteOffset(viewModel.xPad.value.dp, viewModel.yPad.value.dp)
//                    .border(1.dp, androidx.compose.ui.graphics.Color.Blue)
//            )
//        }
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState()),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Top,
//        ) {
//            Text(
//                "Mohon sesuaikan kotak pada area yang ingin di-capture ",
//                fontSize = 20.sp,
//                textAlign = TextAlign.Center,
//                modifier = Modifier
//                    .padding(10.dp)
//            )
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                Text("Width (${viewModel.widthRect.value}) : ")
//
//                Button(onClick = {
//                    viewModel.updateWidthRect(viewModel.widthRect.value + 10)
//                }) {
//                    Text("+")
//                }
//                Button(onClick = {
//                    viewModel.updateWidthRect(viewModel.widthRect.value - 10)
//                }) {
//                    Text("-")
//                }
//            }
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                Text("Height (${viewModel.heightRect.value}) : ")
//
//                Button(onClick = {
//                    viewModel.updateHeightRect(viewModel.heightRect.value + 10)
//                }) {
//                    Text("+")
//                }
//                Button(onClick = {
//                    viewModel.updateHeightRect(viewModel.heightRect.value - 10)
//                }) {
//                    Text("-")
//                }
//            }
//
//            Column(
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Row {
//                    Button(onClick = {
//                        viewModel.updateYPad(viewModel.yPad.value - 10)
//                    }) {
//                        Text("Atas")
//                    }
//                }
//                Row {
//                    Button(onClick = {
//                        viewModel.updateXPad(viewModel.xPad.value - 10)
//                    }) {
//                        Text("Kiri")
//                    }
//                    Button(onClick = {
//                        viewModel.updateXPad(viewModel.xPad.value + 10)
//                    }) {
//                        Text("Kanan")
//                    }
//                }
//                Row {
//                    Button(onClick = {
//                        viewModel.updateYPad(viewModel.yPad.value + 10)
//                    }) {
//                        Text("Bawah")
//                    }
//                }
//
//            }
//
//            Button(onClick = {
//                (ctx as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//                onBackPressed()
//            }) {
//                Text("Simpan")
//            }
//
//            Button(onClick = {
//
//                val tempDir =
//                    ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//
//                // Create a temporary file
//                var tempFile: File? = null
//                try {
//                    tempFile = File.createTempFile("temp_image", ".jpg", tempDir)
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//                if (tempFile != null) {
//                    try {
//                        // Write the bitmap data to the file
//                        val fos = FileOutputStream(tempFile)
//                        viewModel.previewView.value!!.bitmap?.compress(
//                            Bitmap.CompressFormat.PNG,
//                            100,
//                            fos
//                        )
//
//                        fos.flush()
//                        fos.close()
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//
//                Toast.makeText(ctx, "Berhasil menyimpan gambar", Toast.LENGTH_SHORT)
//                    .show()
//
//            }) {
//                Text("Save")
//            }
//
//        }
//
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun SettingCameraPreview2() {
//    PotholeTrackerTheme {
//        Box(
//            modifier = Modifier
//                .padding(20.dp)
//                .safeContentPadding()
//        ) {
//            SettingCameraScreen({})
//        }
//    }
//}
