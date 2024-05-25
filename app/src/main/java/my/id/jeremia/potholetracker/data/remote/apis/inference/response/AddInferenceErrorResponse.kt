package my.id.jeremia.potholetracker.data.remote.apis.inference.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddInferenceErrorResponse(
    @Json(name = "message")
    val message: String?,
    @Json(name = "reason")
    val reason: Reason?,
    @Json(name = "status")
    val status: String?
) {
    @JsonClass(generateAdapter = true)
    data class Reason(
        @Json(name = "0.latitude")
        val latitude: List<String?>?,
        @Json(name = "0.longitude")
        val longitude: List<String?>?,
        @Json(name = "0.status")
        val status: List<String?>?,
        @Json(name = "0.timestamp")
        val timestamp: List<String?>?,
        @Json(name = "0.url")
        val url: List<String?>?
    )
}