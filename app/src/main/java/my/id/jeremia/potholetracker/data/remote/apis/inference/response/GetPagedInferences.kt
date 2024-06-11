package my.id.jeremia.potholetracker.data.remote.apis.inference.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetPagedInferences(
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
        @Json(name = "current_page")
        val currentPage: Int?,
        @Json(name = "data")
        val `data`: List<Data?>?,
        @Json(name = "first_page_url")
        val firstPageUrl: String?,
        @Json(name = "from")
        val from: Int?,
        @Json(name = "last_page")
        val lastPage: Int?,
        @Json(name = "last_page_url")
        val lastPageUrl: String?,
        @Json(name = "links")
        val links: List<Link?>?,
        @Json(name = "next_page_url")
        val nextPageUrl: String?,
        @Json(name = "path")
        val path: String?,
        @Json(name = "per_page")
        val perPage: Int?,
        @Json(name = "prev_page_url")
        val prevPageUrl: Any?,
        @Json(name = "to")
        val to: Int?,
        @Json(name = "total")
        val total: Int?
    ) {
        @JsonClass(generateAdapter = true)
        data class Data(
            @Json(name = "created_at")
            val createdAt: String?,
            @Json(name = "id")
            val id: String?,
            @Json(name = "isRejected")
            val isRejected: Int?,
            @Json(name = "isVerified")
            val isVerified: Int?,
            @Json(name = "latitude")
            val latitude: String?,
            @Json(name = "longitude")
            val longitude: String?,
            @Json(name = "status")
            val status: String?,
            @Json(name = "streetname")
            val streetname: String?,
            @Json(name = "timestamp")
            val timestamp: Long?,
            @Json(name = "updated_at")
            val updatedAt: String?,
            @Json(name = "url")
            val url: String?,
            @Json(name = "userid")
            val userid: Int?
        )

        @JsonClass(generateAdapter = true)
        data class Link(
            @Json(name = "active")
            val active: Boolean?,
            @Json(name = "label")
            val label: String?,
            @Json(name = "url")
            val url: String?
        )
    }
}