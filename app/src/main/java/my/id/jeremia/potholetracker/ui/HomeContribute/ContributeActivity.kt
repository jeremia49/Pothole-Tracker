package my.id.jeremia.potholetracker.ui.HomeContribute

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import my.id.jeremia.potholetracker.databinding.ActivityContributeBinding
import my.id.jeremia.potholetracker.utils.camera.Analyzer
import java.lang.Thread.sleep
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.floor

@AndroidEntryPoint
class ContributeActivity : ComponentActivity() {

    val viewModel: ContributeViewModel by viewModels()
    private lateinit var viewBinding: ActivityContributeBinding

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityContributeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.isCameraActive.observe(this) {
            if (it) {
                viewBinding.cameraicon.visibility = View.VISIBLE
            } else {
                viewBinding.cameraicon.visibility = View.INVISIBLE
            }
        }

        viewModel.isGPSActive.observe(this) {
            if (it) {
                viewBinding.gpsicon.visibility = View.VISIBLE
            } else {
                viewBinding.gpsicon.visibility = View.INVISIBLE
            }
        }

        viewModel.locationData.observe(this){
            updateText()
        }



    }

    fun updateText(){
        viewBinding.contentText.setText(
            "Latitude : ${viewModel.locationData.value?.latitude}\n" +
            "Longitude : ${viewModel.locationData.value?.longitude}\n" +
            "Speed : ${viewModel.locationData.value?.speed?.times(3.6)} km/h\n" +
            "Accuracy : ${viewModel.locationData.value?.accuracy}\n" +
            "Speed Accuracy : ${viewModel.locationData.value?.speedAccuracy}\n"
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            //Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, Analyzer { bp ->
                        sleep(1000)
                        viewModel.setCameraActive()
                        viewModel.addInference(bp)
//                        Log.d(TAG, "called")
//
//                        sleep(2000)

                        sleep(2000)
                    })
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    companion object {
        private const val TAG = "ContributeActivity"
    }

}