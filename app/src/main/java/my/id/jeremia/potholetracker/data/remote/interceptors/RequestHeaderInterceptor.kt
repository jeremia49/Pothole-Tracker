package my.id.jeremia.potholetracker.data.remote.interceptors

import kotlinx.coroutines.runBlocking
import my.id.jeremia.potholetracker.data.remote.RequestHeaders
import my.id.jeremia.potholetracker.data.repository.UserRepository
import my.id.jeremia.potholetracker.di.qualifier.AccessToken
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestHeaderInterceptor @Inject constructor(
    val userRepository: UserRepository,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        val apiAuthType = request.header(RequestHeaders.Key.API_AUTH_TYPE)
        println(apiAuthType)

        builder.removeHeader(RequestHeaders.Key.API_AUTH_TYPE)
        println()

        if(apiAuthType==RequestHeaders.Type.PROTECTED.value){
            runBlocking {
                val accessToken = userRepository.getCurrentAccessToken()
                if (accessToken != null) {
                    builder.addHeader(
                        RequestHeaders.Param.ACCESS_TOKEN.value,
                        "Bearer $accessToken"
                    )
                }
            }
        }

        return chain.proceed(builder.build())
    }

}