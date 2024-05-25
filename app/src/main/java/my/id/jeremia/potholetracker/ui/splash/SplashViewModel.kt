package my.id.jeremia.potholetracker.ui.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import my.id.jeremia.potholetracker.data.remote.apis.auth.AuthAPI
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthErrorResponse
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthLoginErrorResponse
import my.id.jeremia.potholetracker.data.repository.AuthRepository
import my.id.jeremia.potholetracker.data.repository.UserRepository
import my.id.jeremia.potholetracker.di.qualifier.QuickAuthCheck
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
    @QuickAuthCheck private val quickAuthAPI: AuthAPI,
) : BaseViewModel(loader, messenger, navigator) {

    companion object {
        const val TAG = "SplashViewModel"
    }

    init {
        viewModelScope.launch {
            val user = userRepository.getCurrentAuth()
            if (user === null) {
                navigator.navigateTo(Destination.Login.route, true)
            }

            if (user !== null) {
                launchNetwork(error = {
                    try {
                        val error = Moshi.Builder().build().adapter(AuthErrorResponse::class.java)
                            .fromJson(it.fullResponse)

                        if ((error!!.status!! == "error") and (error.message!! == "Unauthenticated.")) {
                            runBlocking {
                                userRepository.removeCurrentUser();
                            }
                            navigator.navigateTo(Destination.Login.route, true)
                        }else{
                            navigator.navigateTo(Destination.Home.Find.route, true)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, e.message.toString())
                        navigator.navigateTo(Destination.Home.Find.route, true)
                    }
                },
                    finish = {
                        viewModelScope.launch {
                            if (userRepository.getCurrentAuth() !== null) {
                                navigator.navigateTo(Destination.Home.Find.route, true)
                            }
                        }
                    }) {
                    flow {
                        emit(quickAuthAPI.me())
                    }.collect {
                        navigator.navigateTo(Destination.Home.Find.route, true)
                    }
                }
            }
        }

    }
}