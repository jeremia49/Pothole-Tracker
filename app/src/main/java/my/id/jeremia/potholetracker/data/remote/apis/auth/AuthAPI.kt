package my.id.jeremia.potholetracker.data.remote.apis.auth


import android.view.PixelCopy.Request
import my.id.jeremia.potholetracker.data.remote.RequestHeaders
import my.id.jeremia.potholetracker.data.remote.apis.auth.request.AuthLoginRequest
import my.id.jeremia.potholetracker.data.remote.apis.auth.request.AuthRegisterRequest
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthLoginSuccessResponse
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthLogoutSuccessResponse
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthMeSuccessResponse
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthRegisterSuccessResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthAPI {

    @POST(Endpoint.LOGIN)
    suspend fun login(
        @Body request: AuthLoginRequest
    ) : AuthLoginSuccessResponse

    @POST(Endpoint.REGISTER)
    suspend fun register(
        @Body request: AuthRegisterRequest
    ) : AuthRegisterSuccessResponse


    @POST(Endpoint.LOGOUT)
    @Headers(RequestHeaders.Key.AUTH_PROTECTED)
    suspend fun logout():AuthLogoutSuccessResponse

    @GET(Endpoint.ME)
    @Headers(RequestHeaders.Key.AUTH_PROTECTED)
    suspend fun me():AuthMeSuccessResponse




}