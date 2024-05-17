package my.id.jeremia.potholetracker.utils.common

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat

fun checkPermission(
    context: Context,
    permissions: Array<String>,
) :Boolean {

    if (
        permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    ) {
        return true;
    }
    return false;
}

fun askPermission(
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
){
    launcher.launch(permissions)
}