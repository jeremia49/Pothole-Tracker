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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import my.id.jeremia.potholetracker.databinding.ActivityContributeBinding
import my.id.jeremia.potholetracker.utils.camera.Analyzer
import java.lang.Thread.sleep
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@AndroidEntryPoint
class ContributeActivity : ComponentActivity(), OnMapReadyCallback {

    val viewModel: ContributeViewModel by viewModels()
    private lateinit var viewBinding: ActivityContributeBinding
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var mMapView: MapView
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    private var googleMap : GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityContributeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mMapView = viewBinding.mapView as MapView;
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

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

        viewModel.locationData.observe(this) {
            updateText()
            if(googleMap!=null){
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(viewModel.locationData.value!!.latitude, viewModel.locationData.value!!.longitude), 17f)
                googleMap!!.clear()
                googleMap!!.addMarker(
                    MarkerOptions()
                        .position(LatLng(viewModel.locationData.value!!.latitude, viewModel.locationData.value!!.longitude))
                        .title("Posisi kamu")
                )
                googleMap!!.animateCamera(cameraUpdate)

            }

        }


    }

    fun updateText() {
        viewBinding.contentText.setText(
            "Latitude : ${viewModel.locationData.value?.latitude}\n" +
                    "Longitude : ${viewModel.locationData.value?.longitude}\n" +
                    "Speed : ${viewModel.locationData.value?.speed?.times(3.6)} km/h\n" +
                    "Accuracy : ${viewModel.locationData.value?.accuracy}\n" +
                    "Speed Accuracy : ${viewModel.locationData.value?.speedAccuracy}\n"
        )
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

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
//        p0.addMarker(
//            MarkerOptions()
//                .position(LatLng(0.0, 0.0))
//                .title("Marker")
//        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle: Bundle? = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }

        mMapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
    }

    override fun onPause() {
        mMapView.onPause()
        super.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
        cameraExecutor.shutdown()
    }

}