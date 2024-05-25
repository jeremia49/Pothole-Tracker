package my.id.jeremia.potholetracker.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import my.id.jeremia.potholetracker.data.local.db.entity.InferenceData
import my.id.jeremia.potholetracker.data.local.db.entity.VerifiedInference

@Dao
interface VerifiedInferenceDao {
    @Insert
    suspend fun insert(entity: VerifiedInference): Long

    @Query("SELECT * FROM verified_inference ORDER BY timestamp DESC")
    fun getAll(): List<VerifiedInference>

    @Query("SELECT * FROM verified_inference WHERE status = :status ORDER BY timestamp DESC")
    fun getAllWithStatus(status:String): List<VerifiedInference>

    @Query("SELECT * FROM verified_inference ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun getAllPaginated(offset: Int, limit: Int): List<VerifiedInference>

    @Query("SELECT * FROM verified_inference WHERE status = :status ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun getAllPaginatedWithStatus(status:String, offset: Int, limit: Int): List<VerifiedInference>

    @Query("SELECT COUNT(*) FROM verified_inference")
    fun getDataCount(): Long

    @Query("DELETE FROM verified_inference")
    suspend fun resetTable():Int
}