package my.id.jeremia.potholetracker.data.remote.apis.auth


import my.id.jeremia.potholetracker.data.remote.apis.auth.request.AuthLoginRequest
import my.id.jeremia.potholetracker.data.remote.apis.auth.request.AuthRegisterRequest
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthLoginSuccessResponse
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthRegisterSuccessResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthAPI {

    @POST(Endpoint.LOGIN)
    @Headers("Content-Type: application/json")
    suspend fun login(
        @Body request: AuthLoginRequest
    ) : AuthLoginSuccessResponse

    @POST(Endpoint.REGISTER)
    @Headers("Content-Type: application/json")
    suspend fun register(
        @Body request: AuthRegisterRequest
    ) : AuthRegisterSuccessResponse





}