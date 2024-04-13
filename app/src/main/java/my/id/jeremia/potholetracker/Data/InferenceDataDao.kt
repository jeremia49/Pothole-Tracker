package my.id.jeremia.potholetracker.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class InferenceDataDao {

    @Insert(onConflict =OnConflictStrategy.IGNORE)
    abstract suspend fun insertInference(inferenceData: InferenceData)

    @Query("SELECT * From `inference-table`")
    abstract fun getAllInferences() : Flow<List<InferenceData>>

    @Query("SELECT * From `inference-table` WHERE status='berlubang'")
    abstract fun getAllJalanBerlubangInferences() : Flow<List<InferenceData>>

    @Query("SELECT * From `inference-table` ORDER BY `timestamp` DESC")
    abstract fun getAllInferencesSORTED() : Flow<List<InferenceData>>

    @Query("SELECT * From `inference-table` WHERE id=:id")
    abstract fun getAnInferenceByID(id:Long) : Flow<InferenceData>

    @Update
    abstract fun updateAnInference(inferenceData:InferenceData)

    @Delete
    abstract suspend fun deleteInference(inferenceData: InferenceData)

    @Query("DELETE FROM `inference-table`")
    abstract suspend fun purgeTable()

}