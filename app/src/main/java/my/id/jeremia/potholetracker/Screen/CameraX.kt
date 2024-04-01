package my.id.jeremia.potholetracker.Screen

import android.content.Context
import android.util.Rational
import android.view.Surface
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.ViewPort
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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

@Composable
fun CameraPreviewScreen() {
    val widthRect = remember {
        mutableIntStateOf(1)
    }
    val heightRect = remember {
        mutableIntStateOf(1)
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
//            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            setBackgroundColor(android.graphics.Color.BLACK)
//            scaleType = PreviewView.ScaleType.FIT_CENTER
        }
    }

    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

    LaunchedEffect(lensFacing, widthRect, heightRect) {
        println("Berubah")
        val viewPort = ViewPort.Builder(Rational(widthRect.intValue, heightRect.intValue), Surface.ROTATION_0).build()
        val useCaseGroup = UseCaseGroup.Builder()
            .addUseCase(preview)
            .addUseCase(imageCapture)
            .setViewPort(viewPort)
            .build()

        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, useCaseGroup)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }


    Column(
        modifier = Modifier
    ) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier
                .border(1.dp, Color.Red)
        )

        Row{
            Text("Width")

            Text(widthRect.intValue.toString())

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

        Row{
            Text("Height")

            Text(heightRect.intValue.toString())

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