package my.id.jeremia.potholetracker.data.remote.apis.inference

import my.id.jeremia.potholetracker.data.remote.RequestHeaders
import my.id.jeremia.potholetracker.data.remote.apis.inference.request.AddInferenceRequestItem
import my.id.jeremia.potholetracker.data.remote.apis.inference.response.AddInferenceSuccessResponse
import my.id.jeremia.potholetracker.data.remote.apis.inference.response.GetInferenceSuccessResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface InferenceAPI {

    @GET(Endpoint.ALL_INFERENCE)
    suspend fun getAllInferences(): GetInferenceSuccessResponse


    @POST(Endpoint.ADD_INFERENCE)
    @Headers(RequestHeaders.Key.AUTH_PROTECTED)
    suspend fun addInference(
        @Body request: List<AddInferenceRequestItem>
    ): AddInferenceSuccessResponse


}