package my.id.jeremia.potholetracker.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import my.id.jeremia.potholetracker.data.local.db.dao.InferenceDataDao
import my.id.jeremia.potholetracker.data.local.db.entity.InferenceData
import javax.inject.Singleton

@Singleton
@Database(
    entities = [
        InferenceData::class
    ],
    exportSchema = false,
    version = 1,
)
@TypeConverters(Converter::class)
abstract class DatabaseService : RoomDatabase() {

    abstract fun inferenceDataDao(): InferenceDataDao

}