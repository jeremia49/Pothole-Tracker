package my.id.jeremia.potholetracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import my.id.jeremia.potholetracker.data.local.db.DatabaseService
import my.id.jeremia.potholetracker.data.local.db.entity.InferenceData
import my.id.jeremia.potholetracker.data.remote.apis.image.ImageUploadAPI
import my.id.jeremia.potholetracker.data.remote.apis.image.response.ImageUploadSuccessResponse
import my.id.jeremia.potholetracker.data.remote.apis.inference.InferenceAPI
import my.id.jeremia.potholetracker.data.remote.apis.inference.request.AddInferenceRequestItem
import my.id.jeremia.potholetracker.data.remote.apis.inference.response.AddInferenceSuccessResponse
import my.id.jeremia.potholetracker.utils.coroutine.Dispatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class LocalInferenceRepository @Inject constructor(
    private val databaseService: DatabaseService,
    private val dispatcher: Dispatcher,
    private val imageAPI: ImageUploadAPI,
    private val inferenceAPI: InferenceAPI,
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

    suspend fun updateImagePath(url: String, id: Long): Flow<Int> =
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

    suspend fun uploadImage(filepath: String): Flow<ImageUploadSuccessResponse> =
        flow {
            emit(
                imageAPI.uploadImage(
                    photo = MultipartBody.Part.createFormData(
                        name = "photo",
                        filename = filepath,
                        body = File(filepath).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                )
            )
        }

    suspend fun sendInference(inferenceData: InferenceData): Flow<AddInferenceSuccessResponse> =
        flow {
            emit(
                inferenceAPI.addInference(
                    listOf(
                        AddInferenceRequestItem(
                            latitude = inferenceData.latitude,
                            longitude = inferenceData.longitude,
                            confidence = 0f,
                            status = inferenceData.status,
                            timestamp = inferenceData.createdTimestamp,
                            url = inferenceData.remoteImgPath,
                        )
                    )
                )
            )
        }

    suspend fun updateInference(inferenceData: InferenceData): Flow<Int> =
        flow {
            emit(databaseService.inferenceDataDao().updateInferenceData(inferenceData))
        }.flowOn(dispatcher.io())

    suspend fun resetTable() : Flow<Int> =
        flow{
            emit(databaseService.inferenceDataDao().resetTable())
        }.flowOn(dispatcher.io())

}