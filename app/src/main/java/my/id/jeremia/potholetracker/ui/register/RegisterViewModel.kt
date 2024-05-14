package my.id.jeremia.potholetracker.ui.register

import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import my.id.jeremia.potholetracker.data.repository.AuthRepository
import my.id.jeremia.potholetracker.ui.base.BaseViewModel
import my.id.jeremia.potholetracker.ui.common.loader.Loader
import my.id.jeremia.potholetracker.ui.common.snackbar.Messenger
import my.id.jeremia.potholetracker.ui.navigation.Destination
import my.id.jeremia.potholetracker.ui.navigation.Navigator
import my.id.jeremia.potholetracker.utils.common.isValidEmail
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    loader: Loader,
    private val authRepository: AuthRepository,
    val navigator: Navigator,
    val messenger: Messenger,
) : BaseViewModel(loader,messenger,navigator) {

    companion object {
        const val TAG = "RegisterViewModel"
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

    fun dologin(){
        if (validate()) {
            launchNetwork {
                authRepository.doLogin(email.value, password.value)
                    .collect {
                        println(it)
                    }
            }

        }

    }

    fun navRegister(){
        navigator.navigateTo(Destination.Register.route)
    }

    private fun validate(): Boolean {
        var error = false
        if (!email.value.isValidEmail()) _emailError.tryEmit("Invalid Email").run { error = true }
        if (password.value.length < 6) _passwordError.tryEmit("Password length should be at least 6")
            .run { error = true }
        return !error
    }

}