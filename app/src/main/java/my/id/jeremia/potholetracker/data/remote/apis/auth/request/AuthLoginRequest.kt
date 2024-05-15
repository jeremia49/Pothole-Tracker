package my.id.jeremia.potholetracker.data.remote.apis.auth.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class AuthLoginRequest(

    @Json(name = "email")
    val email: String,

    @Json(name = "password")
    val password: String,

    )