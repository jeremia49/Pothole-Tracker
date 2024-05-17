package my.id.jeremia.potholetracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import my.id.jeremia.potholetracker.init.CoilInit
import javax.inject.Inject

@HiltAndroidApp()
class PotholeTrackerApp : Application() {

    companion object{
        const val TAG="PotholeTrackerApp"
    }

    @Inject
    lateinit var coilInit: CoilInit


    override fun onCreate() {
        super.onCreate()
        coilInit.init()
    }
}