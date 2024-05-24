package my.id.jeremia.potholetracker.data.model


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Inference(

    @Json(name="latitude")
    val latitude:Float,

    @Json(name="longitude")
    val longitude:Float,

    @Json(name="url")
    val url:String,

    @Json(name="status")
    val status:String,

    @Json(name="confidence")
    val confidence:Float = 0f,

    @Json(name="timestamp")
    val timestamp:Long = System.currentTimeMillis(),

):Parcelable

