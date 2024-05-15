package my.id.jeremia.potholetracker.data.remote.apis.auth.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthRegisterErrorResponse(
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
        @Json(name = "name")
        val name: List<String?>?,
        @Json(name = "password")
        val password: List<String?>?
    )
}