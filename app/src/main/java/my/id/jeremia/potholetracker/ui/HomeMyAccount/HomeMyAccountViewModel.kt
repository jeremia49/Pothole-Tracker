package my.id.jeremia.potholetracker.ui.HomeMyAccount

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import my.id.jeremia.potholetracker.data.local.datastore.UserDataStore
import my.id.jeremia.potholetracker.data.remote.utils.ForcedLogout
import my.id.jeremia.potholetracker.data.repository.AuthRepository
import my.id.jeremia.potholetracker.data.repository.UserRepository
import my.id.jeremia.potholetracker.ui.base.BaseViewModel
import my.id.jeremia.potholetracker.ui.common.loader.Loader
import my.id.jeremia.potholetracker.ui.common.snackbar.Messenger
import my.id.jeremia.potholetracker.ui.navigation.Navigator
import javax.inject.Inject

@HiltViewModel()
class HomeMyAccountViewModel @Inject constructor(
    val navigator: Navigator,
    val messenger: Messenger,
    val loader: Loader,
    val authRepository: AuthRepository,
    val userRepository: UserRepository,
    val forcedLogout: ForcedLogout,
    val userDataStore: UserDataStore,
) : BaseViewModel(
    loader,
    messenger,
    navigator,
) {

    companion object {
        const val TAG = "HomeMyAccountViewModel"
    }

    val username = userDataStore.getUserName();
    val email = userDataStore.getEmail();

    fun doLogout(){
        launchNetwork(
            finish = {
                viewModelScope.launch{
                    userRepository.removeCurrentUser()
                    forcedLogout.logout()
                }
            }
        ) {
            authRepository.doLogout()
                .collect{
                    println(it)
                }
        }
    }

}