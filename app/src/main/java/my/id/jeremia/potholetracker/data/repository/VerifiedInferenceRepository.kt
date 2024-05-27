package my.id.jeremia.potholetracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import my.id.jeremia.potholetracker.data.local.datastore.VerifiedInferenceDataStore
import my.id.jeremia.potholetracker.data.local.db.DatabaseService
import my.id.jeremia.potholetracker.data.local.db.entity.InferenceData
import my.id.jeremia.potholetracker.data.local.db.entity.VerifiedInference
import my.id.jeremia.potholetracker.data.remote.apis.auth.request.AuthLoginRequest
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthLoginSuccessResponse
import my.id.jeremia.potholetracker.data.remote.apis.inference.InferenceAPI
import my.id.jeremia.potholetracker.data.remote.apis.inference.response.GetInferenceSuccessResponse
import my.id.jeremia.potholetracker.utils.coroutine.Dispatcher
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class VerifiedInferenceRepository @Inject constructor(
    private val databaseService: DatabaseService,
    private val dispatcher: Dispatcher,
    private val inferenceAPI: InferenceAPI,
    private val verifiedInferenceDataStore: VerifiedInferenceDataStore
) {

    suspend fun getVerifiedInferenceUpdateTime(): Long = (
            verifiedInferenceDataStore
                .getLastUpdateTime()
                .first() ?: "0"
            ).toLong()

    suspend fun setVerifiedInferenceUpdateTime(s: Long) =
        verifiedInferenceDataStore
            .setLastUpdateTime(s.toString())

    suspend fun insertVerifiedInference(verifiedInference: VerifiedInference) =
        databaseService.verifiedInferenceDao().insert(verifiedInference)
    suspend fun syncFromServer(): Flow<GetInferenceSuccessResponse> =
        flow {
            emit(inferenceAPI.getAllInferences())
        }

    suspend fun fetchAll(): Flow<List<VerifiedInference>> =
        flow {
            emit(
                databaseService.verifiedInferenceDao()
                    .getAll()
            )
        }.flowOn(dispatcher.io())
    suspend fun fetchPaginatedInferences(
        pageNumber: Int,
        pageItemCount: Int
    ): Flow<List<VerifiedInference>> =
        flow {
            emit(
                databaseService.verifiedInferenceDao()
                    .getAllPaginated((pageNumber - 1) * pageItemCount, pageItemCount)
            )
        }.flowOn(dispatcher.io())

    suspend fun resetTable() : Flow<Int> = flow{
        emit(databaseService.verifiedInferenceDao().resetTable())
    }.flowOn(dispatcher.io())
}