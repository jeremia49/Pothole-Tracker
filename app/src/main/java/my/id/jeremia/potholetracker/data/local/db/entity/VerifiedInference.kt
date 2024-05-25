package my.id.jeremia.potholetracker.data.local.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "verified_inference")
data class VerifiedInference(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "longitude")
    val longitude: Float,

    @ColumnInfo(name = "latitude")
    val latitude: Float,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "remote_img_url")
    val remoteImgUrl: String = "",

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    ) : Parcelable