package my.id.jeremia.potholetracker.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inference-table")
data class InferenceData(

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

//    val speed:Float,
//    val accuracy:Float,
//    val speedAccuracy:Float,

    @ColumnInfo(name="localImagePath")
    val localImagePath : String,

    @ColumnInfo(name="isBerlubang")
    val isBerlubang : Boolean=false,

    @ColumnInfo(name="timestamp")
    val timestamp : Long =  System.currentTimeMillis(),

    @PrimaryKey(autoGenerate = true)
    val id:Long = 0L,
)