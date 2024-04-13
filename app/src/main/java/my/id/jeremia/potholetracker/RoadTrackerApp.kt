package my.id.jeremia.potholetracker

import android.app.Application
import my.id.jeremia.potholetracker.Tensorflow.TFlite

class RoadTrackerApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
        TFlite.initializeTFLiteVision(this)
    }


}