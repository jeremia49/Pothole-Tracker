package my.id.jeremia.potholetracker.data.remote.apis.login.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class BasicLoginRequest(

    @Json(name = "email")
    val email: String,

    @Json(name = "password")
    val password: String,

    )