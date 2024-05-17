package my.id.jeremia.potholetracker.data.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import my.id.jeremia.potholetracker.data.local.datastore.UserDataStore
import my.id.jeremia.potholetracker.data.model.Auth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    val userDataStore: UserDataStore,
) {

    suspend fun saveCurrentAuth(auth: Auth) {
        userDataStore.setUserEmail(auth.email)
        userDataStore.setUserId(auth.userid)
        userDataStore.setUserName(auth.username)
        userDataStore.setAccessToken(auth.accessToken)
    }

    suspend fun removeCurrentUser() {
        userDataStore.removeUserId()
        userDataStore.removeUserName()
        userDataStore.removeEmail()
        userDataStore.removeAccessToken()
    }

    suspend fun getCurrentAuth():Auth?{
        val userid = userDataStore.getUserId().first()
        val email = userDataStore.getUserId().first()
        val username = userDataStore.getUserName().first()
        val accesstoken = userDataStore.getAccessToken().first()
        if(userid!==null){
            return Auth(
                userid,
                username!!,
                email!!,
                accesstoken!!
            )
        }
        return null
    }

    suspend fun getCurrentAccessToken():String?{
       return userDataStore.getAccessToken().first()
    }

}