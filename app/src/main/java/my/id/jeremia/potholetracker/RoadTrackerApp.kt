package my.id.jeremia.potholetracker

import android.app.Application

class RoadTrackerApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }


}