package my.id.jeremia.potholetracker.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class AuthLoginResponse(

    @Json(name = "user")
    val data: String,

    @Json(name = "message")
    val message: String,

    @Json(name = "reason")
    val reason: String?,

    @Json(name = "status")
    val status: String
) : Parcelable