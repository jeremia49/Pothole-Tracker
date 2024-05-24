package my.id.jeremia.potholetracker.data.remote.apis.auth.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthErrorResponse(
    @Json(name = "message")
    val message: String?,
    @Json(name = "reason")
    val reason: Any?,
    @Json(name = "status")
    val status: String?
)