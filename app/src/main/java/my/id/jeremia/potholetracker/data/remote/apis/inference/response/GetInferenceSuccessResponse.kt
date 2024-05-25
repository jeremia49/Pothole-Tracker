package my.id.jeremia.potholetracker.data.remote.apis.inference.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetInferenceSuccessResponse(
    @Json(name = "data")
    val `data`: List<Data?>?,
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
        @Json(name = "id")
        val id: String?,
        @Json(name = "latitude")
        val latitude: String?,
        @Json(name = "longitude")
        val longitude: String?,
        @Json(name = "status")
        val status: String?,
        @Json(name = "timestamp")
        val timestamp: String?,
        @Json(name = "updated_at")
        val updatedAt: String?,
        @Json(name = "url")
        val url: String?,
        @Json(name = "userid")
        val userid: Int?
    )
}