package my.id.jeremia.potholetracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import my.id.jeremia.potholetracker.data.model.AuthLoginResponse
import my.id.jeremia.potholetracker.data.remote.apis.login.AuthAPI
import my.id.jeremia.potholetracker.data.remote.apis.login.request.BasicLoginRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authAPI: AuthAPI
) {

    suspend fun doLogin(email:String, password:String):Flow<AuthLoginResponse> =
        flow {
            emit(authAPI.login(BasicLoginRequest(email,password)))
        }

//    suspend fun doRegister(email:String, password:String):Flow<AuthLoginResponse> =
//        flow {
//            emit(authAPI.login(BasicLoginRequest(email,password)))
//        }


}