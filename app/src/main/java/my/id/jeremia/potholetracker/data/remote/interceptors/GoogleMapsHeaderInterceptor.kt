package my.id.jeremia.potholetracker.data.remote.interceptors

import kotlinx.coroutines.runBlocking
import my.id.jeremia.potholetracker.data.remote.RequestHeaders
import my.id.jeremia.potholetracker.di.qualifier.GoogleMapsPlatformApiKey
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleMapsHeaderInterceptor @Inject constructor(
    @GoogleMapsPlatformApiKey val googleMapsApikey : String,
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader(
            "X-Goog-Api-Key",
            googleMapsApikey
        )
        return chain.proceed(builder.build())
    }

}