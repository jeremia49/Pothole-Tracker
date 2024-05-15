package my.id.jeremia.potholetracker.data.remote.apis.login.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthLoginSuccessResponse(
    @Json(name = "data")
    val `data`: Data?,
    @Json(name = "message")
    val message: String?,
    @Json(name = "reason")
    val reason: Any?,
    @Json(name = "status")
    val status: String?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "access_token")
        val accessToken: String?,
        @Json(name = "email")
        val email: String?,
        @Json(name = "name")
        val name: String?,
        @Json(name = "uid")
        val uid: Int?
    )
}