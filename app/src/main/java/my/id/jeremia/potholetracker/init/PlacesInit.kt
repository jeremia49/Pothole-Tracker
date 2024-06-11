package my.id.jeremia.potholetracker.init

import android.content.Context
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.qualifiers.ApplicationContext
import my.id.jeremia.potholetracker.BuildConfig.MAPS_API_KEY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesInit @Inject constructor(
    @ApplicationContext val ctx:Context
) : Initializer {
    override fun init() {
        Places.initializeWithNewPlacesApiEnabled(ctx, MAPS_API_KEY)
    }

}