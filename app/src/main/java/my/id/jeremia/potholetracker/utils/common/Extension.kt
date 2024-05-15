package my.id.jeremia.potholetracker.utils.common

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.inputmethod.InputMethodManager
import androidx.core.util.PatternsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

fun String.Companion.Null() = "null"
fun String.isValidEmail() =
    this.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidUrl(): Boolean {
    return try {
        val uri = Uri.parse(this)
        uri != null && uri.scheme != null
    } catch (e: Exception) {
        false
    }
}


