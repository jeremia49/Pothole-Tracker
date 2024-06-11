package my.id.jeremia.potholetracker.data.remote.apis.inference

import my.id.jeremia.potholetracker.data.remote.RequestHeaders
import my.id.jeremia.potholetracker.data.remote.apis.inference.request.AddInferenceRequestItem
import my.id.jeremia.potholetracker.data.remote.apis.inference.response.AddInferenceSuccessResponse
import my.id.jeremia.potholetracker.data.remote.apis.inference.response.GetInferenceSuccessResponse
import my.id.jeremia.potholetracker.data.remote.apis.inference.response.GetPagedInferences
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface InferenceAPI {

    @GET(Endpoint.ALL_INFERENCE)
    suspend fun getAllInferences(): GetInferenceSuccessResponse
    @GET(Endpoint.ALL_POTHOLES)
    suspend fun getAllPotholes(): GetInferenceSuccessResponse
    @GET(Endpoint.PAGED_INFERENCES)
    suspend fun getPaginatedInferences(
        @Query("page") page: Int = 1,
    ): GetPagedInferences



    @POST(Endpoint.ADD_INFERENCE)
    @Headers(RequestHeaders.Key.AUTH_PROTECTED)
    suspend fun addInference(
        @Body request: List<AddInferenceRequestItem>
    ): AddInferenceSuccessResponse


}