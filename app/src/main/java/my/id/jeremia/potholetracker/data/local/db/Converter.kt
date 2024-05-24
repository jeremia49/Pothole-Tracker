package my.id.jeremia.potholetracker.data.local.db

import androidx.room.TypeConverter
import my.id.jeremia.potholetracker.utils.common.Constants
import java.util.Date

class Converter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date = value?.let { Date(it) } ?: Date()

    @TypeConverter
    fun fromDate(date: Date?): Long = date?.time ?: Constants.NULL

}