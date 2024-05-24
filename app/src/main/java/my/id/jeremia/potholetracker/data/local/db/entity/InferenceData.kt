package my.id.jeremia.potholetracker.data.local.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "inference_data")
data class InferenceData(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "longitude")
    val longitude: Float,

    @ColumnInfo(name = "latitude")
    val latitude: Float,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "localImgPath")
    val localImgPath: String,

    @ColumnInfo(name = "remoteImgPath")
    val remoteImgPath: String = "",

    @ColumnInfo(name = "createdAt")
    val createdAt: Date,

    @ColumnInfo(name = "createdTimestamp")
    val createdTimestamp: Long,

    @ColumnInfo(name = "synced")
    val synced: Boolean = false

    ) : Parcelable
