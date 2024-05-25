package my.id.jeremia.potholetracker.data.remote.apis.inference.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddInferenceRequestItem(
    @Json(name = "confidence")
    val confidence: Float?,
    @Json(name = "latitude")
    val latitude: Float?,
    @Json(name = "longitude")
    val longitude: Float?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "timestamp")
    val timestamp: Long?,
    @Json(name = "url")
    val url: String?
)