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

val Context.verifiedInferenceDataStore: DataStore<Preferences> by preferencesDataStore(name = "verifiedInferenceDataStore")

@Singleton
class VerifiedInferenceDataStore @Inject constructor(
    @ApplicationContext private val ctx: Context,
) {

    companion object {
        private val LAST_UPDATE_TIME = stringPreferencesKey("PREF_KEY_LAST_UPDATE_TIME")
    }


    fun getLastUpdateTime() = ctx.verifiedInferenceDataStore.data.map {
        it[LAST_UPDATE_TIME]
    }
    suspend fun setLastUpdateTime(lastUpdateTime:String) {
        ctx.userDataStore.edit { prefs ->
            prefs[LAST_UPDATE_TIME] = lastUpdateTime
        }
    }
    suspend fun removeLastUpdateTime(){
        ctx.userDataStore.edit { prefs ->
            prefs.remove(LAST_UPDATE_TIME)
        }
    }


}