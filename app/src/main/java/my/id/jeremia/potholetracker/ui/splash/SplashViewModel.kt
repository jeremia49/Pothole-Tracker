package my.id.jeremia.potholetracker.ui.splash

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.data.repository.UserRepository
import my.id.jeremia.potholetracker.ui.base.BaseViewModel
import my.id.jeremia.potholetracker.ui.common.loader.Loader
import my.id.jeremia.potholetracker.ui.common.snackbar.Messenger
import my.id.jeremia.potholetracker.ui.navigation.Destination
import my.id.jeremia.potholetracker.ui.navigation.Navigator
import java.lang.Thread.sleep
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val loader: Loader,
    val messenger: Messenger,
    val navigator: Navigator,
    val userRepository: UserRepository,
) : BaseViewModel(loader, messenger, navigator) {

    companion object {
        const val TAG = "SplashViewModel"
    }

    init {
        viewModelScope.launch{
            val user = userRepository.getCurrentAuth()
            delay(1500)
            if(user === null){
                navigator.navigateTo(Destination.Login.route, true)
            }else{
                navigator.navigateTo(Destination.Home.Find.route, true)
            }
        }

    }
}