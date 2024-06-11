package my.id.jeremia.potholetracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import my.id.jeremia.potholetracker.data.remote.apis.auth.AuthAPI
import my.id.jeremia.potholetracker.data.remote.apis.auth.request.AuthLoginRequest
import my.id.jeremia.potholetracker.data.remote.apis.auth.request.AuthRegisterRequest
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthLoginSuccessResponse
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthLogoutSuccessResponse
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthMeSuccessResponse
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthRegisterSuccessResponse
import my.id.jeremia.potholetracker.di.qualifier.QuickAuthCheck
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authAPI: AuthAPI,
) {

        suspend fun doLogin(email:String, password:String):Flow<AuthLoginSuccessResponse> =
            flow {
                emit(authAPI.login(AuthLoginRequest(email,password)))
            }

    suspend fun doRegister(email:String, name:String, password:String, ):Flow<AuthRegisterSuccessResponse> =
        flow {
            emit(authAPI.register(AuthRegisterRequest(email,name,password)))
        }

    suspend fun doLogout():Flow<AuthLogoutSuccessResponse> =
        flow{
            emit(authAPI.logout())
        }

    suspend fun doCheckMe():Flow<AuthMeSuccessResponse> =
        flow{
            emit(authAPI.me())
        }


}