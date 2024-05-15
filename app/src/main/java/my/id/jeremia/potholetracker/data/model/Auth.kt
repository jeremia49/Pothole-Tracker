package my.id.jeremia.potholetracker.data.model

import android.os.Parcelable
import androidx.datastore.preferences.core.stringPreferencesKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
data class Auth(
    val userid:String,
    val username:String,
    val email:String,
    val accessToken:String,
) : Parcelable