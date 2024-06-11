package my.id.jeremia.potholetracker.ui.HomeFind.NavigationView

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.databinding.ActivityContributeBinding
import my.id.jeremia.potholetracker.databinding.ActivityNavigationBinding
import my.id.jeremia.potholetracker.ui.HomeContribute.ContributeActivity.ContributeViewModel
import my.id.jeremia.potholetracker.ui.HomeFind.HomeFindViewModel
import my.id.jeremia.potholetracker.utils.googlemaps.isPointOnPolyline
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory

@AndroidEntryPoint
class NavigationActivity : ComponentActivity() {

    private lateinit var viewBinding: ActivityNavigationBinding

    val viewModel: NavigationViewModel by viewModels()

    private lateinit var mMapView: MapView
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    private var googleMap: GoogleMap? = null
    private lateinit var placeID: String

    private lateinit var myLocationMarker: Marker
    private var potholeMarker: Marker? = null


    private var isMyLocationInitialized: Boolean = false
    private var shouldFollowUserPosition: Boolean = true

    fun initializeMarker() {
        myLocationMarker = googleMap!!.addMarker(
            MarkerOptions()
                .position(
                    LatLng(
                        viewModel.locationData.value!!.latitude,
                        viewModel.locationData.value!!.longitude
                    )
                )
                .title("Posisi kamu")
        )!!
    }


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        placeID = getIntent().getStringExtra("placeId")!!
        viewModel.placeID.value = placeID

        viewBinding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mMapView = viewBinding.mapView
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync { p0 ->
            googleMap = p0

            googleMap!!.setMyLocationEnabled(true)
            googleMap!!.setPadding(10, 50, 10, 10)

            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                HomeFindViewModel.siantarCamPos, 13f
            )
            googleMap!!.animateCamera(cameraUpdate)

            googleMap!!.setOnCameraMoveStartedListener { reason ->
                println("Reason $reason")
                if (reason == 1) {
                    shouldFollowUserPosition = false
                }
                println("$shouldFollowUserPosition")
            }

            googleMap!!.setOnMyLocationButtonClickListener(
                GoogleMap.OnMyLocationButtonClickListener {
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        LatLng(viewModel.locationData.value!!.latitude, viewModel.locationData.value!!.longitude), 17f
                    )
                    googleMap!!.animateCamera(cameraUpdate)

                    shouldFollowUserPosition = true
                    true
                }
            )

        };

        viewModel.isLoading.observe(this) {
            if (it) {
                viewBinding.loading.visibility = View.VISIBLE
            } else {
                viewBinding.loading.visibility = View.GONE
            }
        }

        viewModel.locationData.observe(this, object : Observer<Location> {
            override fun onChanged(value: Location) {
                if (googleMap != null) {

                    initializeMarker()

                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            value.latitude,
                            value.longitude
                        ), 13f
                    )
                    googleMap!!.animateCamera(cameraUpdate)

                    viewModel.startGetRoute()
                }
                viewModel.locationData.removeObserver(this)
            }
        })

        viewModel.locationData.observe(this) {
            if (googleMap != null) {

                myLocationMarker.position = LatLng(it.latitude, it.longitude)

                if (shouldFollowUserPosition) {
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            it.latitude,
                            it.longitude
                        ), 17f
                    )
                    googleMap!!.animateCamera(cameraUpdate)
                }

                if (viewModel.kdTree != null) {

                    val (nn, ssq, nv) = viewModel.kdTree!!.nearest(
                        doubleArrayOf(it.latitude, it.longitude)
                    )

                    println("Nearest neighbor : ${nn?.asList()}")
                    println("Distance         : ${Math.sqrt(ssq)}")
                    println("Nodes visited    : $nv")

                    val results = floatArrayOf(0f,0f)
                    Location.distanceBetween(
                        it.latitude,
                        it.longitude,
                        nn!![0],
                        nn[1],
                        results
                    )
                    println("Results : ${results.contentToString()}")

                    val distance = results[0]
                    val bearing = if(results.size > 2){
                            results[2]
                        }else if(results.size > 1){
                            results[1]
                        }else{
                            0f
                    }
                    println("distance : $distance")
                    println("bearing : $bearing")
                    if(bearing>0f&&bearing<=180f){
                        println("Lubang dalam ${distance}m")
                    }


                    if(potholeMarker == null){
                        potholeMarker = googleMap!!.addMarker(
                            MarkerOptions()
                                .position(
                                    LatLng(
                                        nn!![0],
                                        nn[1]
                                    )))!!
                    }else{
                        potholeMarker!!.position = LatLng(nn!![0], nn[1])
                    }

                }
            }
        }

        viewModel.routeData.observe(this) {
            try {
                val polyline = PolyUtil.decode(it.routes!![0]!!.polyline!!.encodedPolyline)
                googleMap!!.addPolyline(
                    PolylineOptions()
                        .addAll(polyline)
                        .width(10f)
                        .color(Color.BLUE)
                )

                val polylineArray : Array<Coordinate> = emptyArray()
                for (p in polyline) {
                    polylineArray.plus(
                        Coordinate(p.latitude, p.longitude)
                    )
                }

                val geometryFactory = GeometryFactory()
                val lineString = geometryFactory.createLineString(polylineArray)

                val inferencesOnPolyline = viewModel.inferencesBerlubang.filter{inf->
                    isPointOnPolyline(Coordinate(inf.latitude.toDouble(), inf.longitude.toDouble(),), lineString)
                }

                for(inf in inferencesOnPolyline){
                    googleMap!!.addMarker(
                        MarkerOptions()
                            .position(
                                LatLng(
                                    inf.latitude!!.toDouble(),
                                    inf.longitude!!.toDouble()
                                )
                            )
                            .title("Berlubang")
                    )
                }


                val end = it.routes!![0]?.legs?.get(
                    (it.routes!![0]?.legs?.size ?: 1) - 1
                )?.endLocation?.latLng
                googleMap!!.addMarker(
                    MarkerOptions()
                        .position(
                            LatLng(
                                end!!.latitude!!,
                                end.longitude!!
                            )
                        )
                        .title("Tujuan")
                )
            } catch (_: Exception) {

            }
        }


    }
}