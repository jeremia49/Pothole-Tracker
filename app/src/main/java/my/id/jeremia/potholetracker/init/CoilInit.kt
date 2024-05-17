package my.id.jeremia.potholetracker.init

import coil.Coil
import coil.ImageLoader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoilInit @Inject constructor(
    private val image: ImageLoader
) : Initializer {
    override fun init() {
        Coil.setImageLoader(image)
    }
}