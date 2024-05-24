package my.id.jeremia.potholetracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import my.id.jeremia.potholetracker.data.local.db.DatabaseService
import my.id.jeremia.potholetracker.data.local.db.entity.InferenceData
import my.id.jeremia.potholetracker.data.model.Inference
import my.id.jeremia.potholetracker.utils.coroutine.Dispatcher
import javax.inject.Inject

class LocalInferenceRepository @Inject constructor(
    private val databaseService: DatabaseService,
    private val dispatcher: Dispatcher,
) {

    suspend fun fetchInferences(
        pageNumber: Int,
        pageItemCount: Int
    ): Flow<List<InferenceData>> =
        flow {
            emit(
                databaseService.inferenceDataDao()
                    .getPaginated((pageNumber - 1) * pageItemCount, pageItemCount)
            )
        }.flowOn(dispatcher.io())

    suspend fun saveInference(inferenceData: InferenceData): Flow<Long> =
        flow {
            emit(databaseService.inferenceDataDao().insert(inferenceData))
        }.flowOn(dispatcher.io())

    suspend fun deleteInference(inferenceData: InferenceData): Flow<Int> =
        flow {
            emit(databaseService.inferenceDataDao().delete(inferenceData))
        }.flowOn(dispatcher.io())

//    suspend fun sendInference(inferenceData: List<InferenceData>): Flow<String> =
//        flow {
//            emit(userApi.journalStorage(JournalsRequest(journals)))
//        }.map { it.message }

    suspend fun updateImagePath(url:String,id:Long): Flow<Int> =
        flow {
            emit(databaseService.inferenceDataDao().updateImageRemotePath(url, id))
        }.flowOn(dispatcher.io())

    suspend fun fetchUnsyncedInference(): Flow<List<InferenceData>> =
        flow {
            emit(databaseService.inferenceDataDao().getAllUnSync())
        }.flowOn(dispatcher.io())

    suspend fun markedSynced(inferenceData: List<InferenceData>): Flow<Int> =
        flow {
            emit(databaseService.inferenceDataDao().setAsSynced(inferenceData.map { it.id }))
        }.flowOn(dispatcher.io())


}