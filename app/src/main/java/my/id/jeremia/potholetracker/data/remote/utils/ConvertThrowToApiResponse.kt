package my.id.jeremia.potholetracker.data.remote.utils

import android.util.Log
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import my.id.jeremia.potholetracker.data.remote.response.ApiErrorResponse
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException

const val THROWABLE_API_ERROR_TAG = "THROWABLE_API_ERROR"

fun Throwable.toApiErrorResponse(): ApiErrorResponse {
    val defaultResponse = ApiErrorResponse()
    try {
        return when (this) {
            is ConnectException ->
                return ApiErrorResponse(ApiErrorResponse.Status.REMOTE_CONNECTION_ERROR, 0)

            is NoConnectivityErr ->
                return ApiErrorResponse(ApiErrorResponse.Status.NETWORK_CONNECTION_ERROR, 0)

            is HttpException -> {
                return ApiErrorResponse(
                    ApiErrorResponse.Status.API_ERROR,
                    ApiErrorResponse.Status.API_ERROR.code,
                    "",
                    this.response()?.errorBody()?.string()
                )
            }

            else -> {
                val message = this.message
                if (message != null && message.contains("unexpected end of stream"))
                    return ApiErrorResponse(ApiErrorResponse.Status.REMOTE_CONNECTION_ERROR, 0)
                return defaultResponse
            }
        }
    } catch (e: IOException) {
        Log.e(THROWABLE_API_ERROR_TAG, e.toString())
    } catch (e: JsonDataException) {
        Log.e(THROWABLE_API_ERROR_TAG, e.toString())
    } catch (e: NullPointerException) {
        Log.e(THROWABLE_API_ERROR_TAG, e.toString())
    }
    return defaultResponse
}