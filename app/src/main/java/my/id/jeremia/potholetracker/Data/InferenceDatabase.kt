package my.id.jeremia.potholetracker.Data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [InferenceData::class],
    version = 2,
    exportSchema = false,
)

abstract class InferenceDatabase : RoomDatabase() {
    abstract fun inferenceDataDao() : InferenceDataDao
}