package my.id.jeremia.potholetracker.Data

import kotlinx.coroutines.flow.Flow

class InferenceRepository(private val inferenceDataDao: InferenceDataDao) {

    suspend fun addAnInference(inferenceData: InferenceData){
        inferenceDataDao.insertInference(inferenceData)
    }

    fun getAllInferences(): Flow<List<InferenceData>> = inferenceDataDao.getAllInferences()

    fun getAllJalanBerlubangInferences() : Flow<List<InferenceData>> = inferenceDataDao.getAllJalanBerlubangInferences()

    fun getAllInferencesSORTED() : Flow<List<InferenceData>> = inferenceDataDao.getAllInferencesSORTED()

    fun getAnInferenceById(id:Long) : Flow<InferenceData> = inferenceDataDao.getAnInferenceByID(id)

    suspend fun updateAnInference(inferenceData: InferenceData){
        inferenceDataDao.updateAnInference(inferenceData)
    }

    suspend fun deleteAnInference(inferenceData: InferenceData){
        inferenceDataDao.deleteInference(inferenceData)
    }

    suspend fun deleteAllInferences(){
        inferenceDataDao.purgeTable()
    }



}