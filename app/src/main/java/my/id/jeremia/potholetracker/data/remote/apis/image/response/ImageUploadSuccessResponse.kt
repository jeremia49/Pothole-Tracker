package my.id.jeremia.potholetracker.data.remote.apis.image.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageUploadSuccessResponse(
    @Json(name = "data")
    val `data`: String?,
    @Json(name = "message")
    val message: String?,
    @Json(name = "reason")
    val reason: Any?,
    @Json(name = "status")
    val status: String?
)