package my.id.jeremia.potholetracker.data.remote.apis.login


import my.id.jeremia.potholetracker.data.model.AuthLoginResponse
import my.id.jeremia.potholetracker.data.remote.apis.login.request.BasicLoginRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthAPI {

    @POST(Endpoint.LOGIN)
    @Headers("Content-Type: application/json")
    suspend fun login(
        @Body request: BasicLoginRequest
    ) : AuthLoginResponse

}