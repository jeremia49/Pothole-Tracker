package my.id.jeremia.potholetracker.Screen

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.util.Rational
import android.view.Surface
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.ViewPort
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.view.setPadding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors

@Composable
fun CameraPreviewScreen() {
    val widthRect = remember {
        mutableIntStateOf(100)
    }
    val heightRect = remember {
        mutableIntStateOf(100)
    }
    val xPad = remember {
        mutableIntStateOf(0)
    }
    val yPad = remember {
        mutableIntStateOf(0)
    }


    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val preview = Preview
        .Builder()
        .build()

    val imageCapture = ImageCapture
        .Builder()
        .build()


    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FIT_CENTER
            setBackgroundColor(android.graphics.Color.BLACK)
            setPadding(1)
        }
    }

    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

    LaunchedEffect(lensFacing) {
        val viewPort = ViewPort.Builder(Rational(16, 9), Surface.ROTATION_0).build()

        val useCaseGroup = UseCaseGroup.Builder()
            .addUseCase(preview)
            .addUseCase(imageCapture)
            .setViewPort(viewPort)
            .build()

        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, useCaseGroup)
        preview.setSurfaceProvider(previewView.surfaceProvider)

        widthRect.intValue = previewView.width
        heightRect.intValue = previewView.height
    }


    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {

        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center,
        ) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier
                    .defaultMinSize(100.dp, 100.dp)
                    .border(1.dp, Color.Red)
            )
            Box(
                modifier =
                Modifier
                    .width(widthRect.intValue.dp)
                    .height(heightRect.intValue.dp)
                    .absoluteOffset(xPad.intValue.dp, yPad.intValue.dp)
                    .border(1.dp, Color.Blue)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                "Mohon sesuaikan kotak pada area yang ingin di-capture ",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Width (${widthRect.intValue}) : ")

                Button(onClick = {
                    widthRect.intValue += 10
                }) {
                    Text("+")
                }
                Button(onClick = {
                    widthRect.intValue -= 10
                }) {
                    Text("-")
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Height (${heightRect.intValue}) : ")
                Button(onClick = {
                    heightRect.intValue += 10
                }) {
                    Text("+")
                }
                Button(onClick = {
                    heightRect.intValue -= 10
                }) {
                    Text("-")
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row {
                    Button(onClick = {
                        yPad.intValue -= 10
                    }) {
                        Text("Atas")
                    }
                }
                Row {
                    Button(onClick = {
                        xPad.intValue -= 10
                    }) {
                        Text("Kiri")
                    }
                    Button(onClick = {
                        xPad.intValue += 10
                    }) {
                        Text("Kanan")
                    }
                }
                Row {
                    Button(onClick = {
                        yPad.intValue += 10
                    }) {
                        Text("Bawah")
                    }
                }


            }


            Row {
                Button(onClick = {

                    val tempDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

                    // Create a temporary file
                    var tempFile: File? = null
                    try {
                        tempFile = File.createTempFile("temp_image", ".jpg", tempDir)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (tempFile != null) {
                        try {
                            // Write the bitmap data to the file
                            val fos = FileOutputStream(tempFile)
                            previewView.bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
                            fos.flush()
                            fos.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }

                    Toast.makeText(context, "Berhasil menyimpan gambar", Toast.LENGTH_SHORT).show()

                }) {
                    Text("Save")
                }
            }
        }


    }

}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }