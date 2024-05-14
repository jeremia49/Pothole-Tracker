package my.id.jeremia.potholetracker.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.data.remote.utils.ForcedLogout
import my.id.jeremia.potholetracker.ui.base.BaseViewModel
import my.id.jeremia.potholetracker.ui.common.loader.Loader
import my.id.jeremia.potholetracker.ui.common.snackbar.Messenger
import my.id.jeremia.potholetracker.ui.navigation.Destination
import my.id.jeremia.potholetracker.ui.navigation.Navigator
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    val forcedLogout: ForcedLogout,
    val loader: Loader,
    val navigator:Navigator,
    val messenger: Messenger,
) :BaseViewModel(loader,messenger, navigator){

    companion object {
        const val TAG = "MainViewModel"
    }

    init {
        viewModelScope.launch {
            forcedLogout.state
                .collect {
                    if (it) {
//                        userRepository.removeCurrentUser()
                        navigator.navigateTo(Destination.Login.route, true)
                    }
                }
        }
    }

}