package my.id.jeremia.potholetracker.data.model

data class Location(
    val latitude: Double,
    val longitude: Double,
    val speed:Float=0f,
    val accuracy:Float=0f,
    val speedAccuracy:Float=0f,
    val bearing:Float=0f,
)