package my.id.jeremia.potholetracker.data.model

data class NearestPothole(
    val latitude: Double,
    val longitude: Double,
    val title: String? = null,
    val imgUrl: String? = null,
    val distance: Float = 0f,
    val bearing: Float = 0f,
    val willPassBy:Boolean = false,
)
