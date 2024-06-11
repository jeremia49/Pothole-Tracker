package my.id.jeremia.potholetracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import my.id.jeremia.potholetracker.init.CoilInit
import my.id.jeremia.potholetracker.init.PlacesInit
import javax.inject.Inject

@HiltAndroidApp()
class PotholeTrackerApp : Application() {

    companion object{
        const val TAG="PotholeTrackerApp"
    }

    @Inject
    lateinit var coilInit: CoilInit

    @Inject
    lateinit var placesInit : PlacesInit


    override fun onCreate() {
        super.onCreate()
        coilInit.init()
        placesInit.init()
    }
}