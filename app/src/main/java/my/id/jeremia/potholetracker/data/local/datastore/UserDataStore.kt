package my.id.jeremia.potholetracker.data.local.datastore

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

//val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "userDataStore")

@Singleton
class UserDataStore @Inject constructor(
    @ApplicationContext private val ctx: Context,
) {

    companion object {
        private val ON_BOARDING_COMPLETED =
            stringPreferencesKey("PREF_KEY_USER_ON_BOARDING_COMPLETED")
        private val USER_ID = stringPreferencesKey("PREF_KEY_USER_ID")
        private val USER_NAME = stringPreferencesKey("PREF_KEY_USER_NAME")
        private val USER_EMAIL = stringPreferencesKey("PREF_KEY_USER_EMAIL")

        private val ACCESS_TOKEN = stringPreferencesKey("PREF_KEY_ACCESS_TOKEN")
    }


    fun getUserId() = ctx.userDataStore.data.map {
        it[USER_ID]
    }
    suspend fun setUserId(userID:String) {
        ctx.userDataStore.edit { prefs ->
            prefs[USER_ID] = userID
        }
    }
    suspend fun removeUserId(){
        ctx.userDataStore.edit { prefs ->
            prefs.remove(USER_ID)
        }
    }


    fun getOnBoardingCompleted() = ctx.userDataStore.data.map {
        it[ON_BOARDING_COMPLETED]
    }
    suspend fun setOnBoardingCompleted(onBoarding:String) {
        ctx.userDataStore.edit { prefs ->
            prefs[ON_BOARDING_COMPLETED] = onBoarding
        }
    }
    suspend fun removeOnBoardingCompleted(){
        ctx.userDataStore.edit { prefs ->
            prefs.remove(USER_ID)
        }
    }


    fun getUserName() = ctx.userDataStore.data.map {
        it[USER_NAME]
    }
    suspend fun setUserName(username:String) {
        ctx.userDataStore.edit { prefs ->
            prefs[USER_NAME] = username
        }
    }
    suspend fun removeUserName(){
        ctx.userDataStore.edit { prefs ->
            prefs.remove(USER_NAME)
        }
    }


    fun getEmail() = ctx.userDataStore.data.map {
        it[USER_EMAIL]
    }
    suspend fun setUserEmail(email:String) {
        ctx.userDataStore.edit { prefs ->
            prefs[USER_EMAIL] = email
        }
    }
    suspend fun removeEmail(){
        ctx.userDataStore.edit { prefs ->
            prefs.remove(USER_EMAIL)
        }
    }


    fun getAccessToken() = ctx.userDataStore.data.map {
        it[ACCESS_TOKEN]
    }
    suspend fun setAccessToken(accToken:String) {
        ctx.userDataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accToken
        }
    }
    suspend fun removeAccessToken(){
        ctx.userDataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
        }
    }


}