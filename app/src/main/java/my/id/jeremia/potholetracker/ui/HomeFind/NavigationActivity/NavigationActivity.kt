package my.id.jeremia.potholetracker.ui.HomeFind.NavigationActivity

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import coil.load
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.databinding.ActivityNavigationBinding
import my.id.jeremia.potholetracker.ui.HomeFind.HomeFindViewModel
import my.id.jeremia.potholetracker.utils.googlemaps.isPointOnPolyline
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import kotlin.math.abs

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

    private lateinit var mp : MediaPlayer;

    fun initializeMarker() {
        myLocationMarker = googleMap!!.addMarker(
            MarkerOptions()
                .icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )
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

        placeID = getIntent().getStringExtra("placeId") ?: "ChIJ6xjGDS-fMTARsNkgsoCdAwQ"
        viewModel.placeID.value = placeID

        viewBinding = ActivityNavigationBinding.inflate(layoutInflater)

        mp = MediaPlayer.create(this, R.raw.notification_sound)

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            viewBinding.main.apply {
                setPaddingRelative(paddingStart, paddingTop, paddingEnd, systemBars.bottom)
            }
            return@setOnApplyWindowInsetsListener insets
        }
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
                if (reason == 1) {
                    shouldFollowUserPosition = false
                }
            }

            googleMap!!.setOnMyLocationButtonClickListener(
                GoogleMap.OnMyLocationButtonClickListener {
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            viewModel.locationData.value!!.latitude,
                            viewModel.locationData.value!!.longitude
                        ), 17f
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

                    viewModel.stopLoading()
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


        viewModel.overlappedInferences.observe(this) {
            for (inf in it) {
                googleMap!!.addMarker(
                    MarkerOptions()
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                        )
                        .position(
                            LatLng(
                                inf.latitude!!.toDouble(),
                                inf.longitude!!.toDouble()
                            )
                        )
                        .title("Berlubang")
                )
            }
        }

        viewModel.currentClosestPothole.observe(this) {

            if (it.distance > 200 ) {
                viewBinding.containerwarning.visibility = View.INVISIBLE
                return@observe
            }

            if(!it.willPassBy){
                viewBinding.containerwarning.visibility = View.INVISIBLE
                return@observe
            }

            mp.start()
            viewBinding.containerwarning.visibility = View.VISIBLE

//            viewBinding.potholeImage.load("https://picsum.photos/200/300")

            val text =
                "Ada lubang di depan!\nLat : ${it.latitude}\nLong : ${it.longitude}\nDistance: ${it.distance}m\nBearing: ${it.bearing}"
            viewBinding.text.text = text


            if (potholeMarker == null) {
                potholeMarker = googleMap!!.addMarker(
                    MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .position(
                            LatLng(
                                it.latitude,
                                it.longitude,
                            )
                        )
                )!!
            } else {
                potholeMarker!!.position = LatLng(
                    it.latitude,
                    it.longitude,
                )
            }

        }

    }

    override fun onDestroy() {
        mp.release()
        super.onDestroy()
    }

}