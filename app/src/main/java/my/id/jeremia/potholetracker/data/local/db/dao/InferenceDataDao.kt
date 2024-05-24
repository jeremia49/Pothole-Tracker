package my.id.jeremia.potholetracker.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import my.id.jeremia.potholetracker.data.local.db.entity.InferenceData

@Dao
interface InferenceDataDao {

    @Delete
    suspend fun delete(entity: InferenceData): Int
    //emits an int value, indicating the number of rows removed from the database.

    @Insert
    suspend fun insert(entity: InferenceData): Long
    // emits a long, which is the new rowId for the inserted item

    @Query("SELECT * FROM inference_data ORDER BY createdAt DESC")
    fun getAll(): List<InferenceData>

    @Query("SELECT * FROM inference_data WHERE synced = 0 ORDER BY createdAt DESC")
    fun getAllUnSync(): List<InferenceData>

    @Query("SELECT * FROM inference_data ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    fun getPaginated(offset: Int, limit: Int): List<InferenceData>

    @Query("UPDATE inference_data SET synced = 1 WHERE id IN (:ids)")
    suspend fun setAsSynced(ids: List<Long>): Int

}