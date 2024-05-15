package my.id.jeremia.potholetracker.data.remote.apis.login.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthLoginErrorResponse(
    @Json(name = "message")
    val message: String?,
    @Json(name = "reason")
    val reason: Reason?,
    @Json(name = "status")
    val status: String?
) {
    @JsonClass(generateAdapter = true)
    data class Reason(
        @Json(name = "email")
        val email: List<String?>?,
        @Json(name = "password")
        val password: List<String?>?
    )
}