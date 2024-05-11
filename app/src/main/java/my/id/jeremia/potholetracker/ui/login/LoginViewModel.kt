package my.id.jeremia.potholetracker.ui.login

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import my.id.jeremia.potholetracker.ui.base.BaseViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(

) : BaseViewModel() {

    companion object {
        const val TAG = "LoginViewModel"
    }

    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _emailError = MutableStateFlow("")
    private val _passwordError = MutableStateFlow("")

    val email = _email.asStateFlow()
    val password = _password.asStateFlow()
    val emailError = _emailError.asStateFlow()
    val passwordError = _passwordError.asStateFlow()

    fun onEmailChange(input: String) {
        _email.tryEmit(input)
        if (emailError.value.isNotEmpty()) _emailError.tryEmit("")
    }

    fun onPasswordChange(input: String) {
        _password.tryEmit(input)
        if (passwordError.value.isNotEmpty()) _passwordError.tryEmit("")
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    fun basicLogin() {
//        if (validate()) {
//            launchNetwork {
//                authRepository.basicLogin(email.value, password.value)
//                    .flatMapLatest { auth ->
//                        flow {
//                            userRepository.saveCurrentAuth(auth)
//                            val firebaseToken = userRepository.getFirebaseToken()
//                            if (firebaseToken != null && userRepository.userExists()) {
//                                userRepository.sendFirebaseToken(firebaseToken)
//                                    .catch {
//                                        emit(auth)
//                                    }
//                                    .collect {
//                                        userRepository.setFirebaseTokenSent()
//                                        emit(auth)
//                                    }
//                            } else {
//                                emit(auth)
//                            }
//                        }
//                    }
//                    .collect {
//                        messenger.deliver(Message.success("Login Success"))
//                        if (userRepository.isOnBoardingComplete()) {
//                            navigator.navigateTo(Destination.Home.route, true)
//                        } else {
//                            navigator.navigateTo(Destination.Onboarding.route, true)
//                        }
//                    }
//            }
//        }
//    }
//
//    private fun validate(): Boolean {
//        var error = false
//        if (!email.value.isValidEmail()) _emailError.tryEmit("Invalid Email").run { error = true }
//        if (password.value.length < 6) _passwordError.tryEmit("Password length should be at least 6")
//            .run { error = true }
//        return !error
//    }
}