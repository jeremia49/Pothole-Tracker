package my.id.jeremia.potholetracker.data.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Inference(
    val lat:Float,
    val long:Float,
    val imgUrl:String,
    val status:String,
):Parcelable

