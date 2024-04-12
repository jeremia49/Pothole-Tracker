package my.id.jeremia.potholetracker.utils

import java.sql.Timestamp
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun timestampToFormattedString(timestamp:Long):String{
    val timeStamp = Timestamp(timestamp)
    return timeStamp.toString()
}