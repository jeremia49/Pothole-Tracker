package my.id.jeremia.potholetracker.data.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image (
    val image:Bitmap
) : Parcelable