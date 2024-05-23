package my.id.jeremia.potholetracker.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.cropImageDataStore: DataStore<Preferences> by preferencesDataStore(name = "cropImageDataStore")

@Singleton
class CropDataStore @Inject constructor(
    @ApplicationContext private val ctx: Context,
) {
    companion object {
        private val LEFT = stringPreferencesKey("PREF_KEY_LEFT")
        private val TOP = stringPreferencesKey("PREF_KEY_TOP")
        private val RIGHT = stringPreferencesKey("PREF_KEY_RIGHT")
        private val BOTTOM = stringPreferencesKey("PREF_KEY_BOTTOM")
    }

    fun getLeft() = ctx.cropImageDataStore.data.map {
        it[LEFT]
    }

    fun getTop() = ctx.cropImageDataStore.data.map {
        it[TOP]
    }

    fun getRight() = ctx.cropImageDataStore.data.map {
        it[RIGHT]
    }

    fun getBottom() = ctx.cropImageDataStore.data.map {
        it[BOTTOM]
    }


    suspend fun setLeft(value: String) {
        ctx.cropImageDataStore.edit { prefs ->
            prefs[LEFT] = value
        }
    }

    suspend fun setTop(value: String) {
        ctx.cropImageDataStore.edit { prefs ->
            prefs[TOP] = value
        }
    }
    suspend fun setRight(value: String) {
        ctx.cropImageDataStore.edit { prefs ->
            prefs[RIGHT] = value
        }
    }
    suspend fun setBottom(value: String) {
        ctx.cropImageDataStore.edit { prefs ->
            prefs[BOTTOM] = value
        }
    }




}