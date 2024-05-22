package my.id.jeremia.potholetracker.ui.HomeContribute

import android.Manifest
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import my.id.jeremia.potholetracker.ui.base.BaseViewModel
import my.id.jeremia.potholetracker.ui.common.loader.Loader
import my.id.jeremia.potholetracker.ui.common.snackbar.Messenger
import my.id.jeremia.potholetracker.ui.navigation.Navigator
import my.id.jeremia.potholetracker.utils.common.checkPermission
import javax.inject.Inject

@HiltViewModel
class HomeContributeViewModel @Inject constructor(
    val loader: Loader,
    val navigator: Navigator,
    val messenger: Messenger,
    @ApplicationContext val ctx:Context,
): BaseViewModel(
    loader,messenger,navigator
) {

    companion object{
        const val TAG = "HomeContributeViewModel"
    }

    private val _cameraPermission = mutableStateOf(false)
    private val _locationPermission = mutableStateOf(false)
    val cameraPermission = _cameraPermission
    val locationPermission = _locationPermission

    init{
        updatePermissionState()
    }

    fun updatePermissionState(){
        _cameraPermission.value = checkPermission(
            ctx,
            arrayOf(
                Manifest.permission.CAMERA,
            )
        )
        _locationPermission.value = checkPermission(
            ctx,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }


}