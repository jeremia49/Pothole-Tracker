package my.id.jeremia.potholetracker.data.remote.apis.image

import my.id.jeremia.potholetracker.data.remote.RequestHeaders
import my.id.jeremia.potholetracker.data.remote.apis.image.response.ImageUploadSuccessResponse
import okhttp3.MultipartBody
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageUploadAPI {

    @Multipart
    @POST(Endpoint.upload_image)
    @Headers(RequestHeaders.Key.AUTH_PROTECTED)
    suspend fun uploadImage(
        @Part photo: MultipartBody.Part
    ) : ImageUploadSuccessResponse

}