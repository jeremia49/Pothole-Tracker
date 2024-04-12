package my.id.jeremia.potholetracker.Data

import androidx.room.Entity

@Entity(tableName = "inference-table")
data class InferenceData(
    val latitude: Double,
    val longitude: Double,
    val speed:Float,
    val accuracy:Float,
    val speedAccuracy:Float,
    val imagePath : String,
    val berlubang : Boolean=false,
)