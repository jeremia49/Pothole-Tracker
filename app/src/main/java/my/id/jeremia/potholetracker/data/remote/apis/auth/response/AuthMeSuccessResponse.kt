package my.id.jeremia.potholetracker.data.remote.apis.auth.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthMeSuccessResponse(
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
        @Json(name = "created_at")
        val createdAt: String?,
        @Json(name = "email")
        val email: String?,
        @Json(name = "email_verified_at")
        val emailVerifiedAt: Any?,
        @Json(name = "id")
        val id: Int?,
        @Json(name = "name")
        val name: String?,
        @Json(name = "updated_at")
        val updatedAt: String?
    )
}