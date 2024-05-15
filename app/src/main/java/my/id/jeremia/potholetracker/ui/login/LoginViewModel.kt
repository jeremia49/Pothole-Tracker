package my.id.jeremia.potholetracker.ui.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.data.local.datastore.UserDataStore
import my.id.jeremia.potholetracker.data.model.Auth
import my.id.jeremia.potholetracker.data.remote.response.ApiErrorResponse
import my.id.jeremia.potholetracker.data.remote.apis.login.response.AuthLoginErrorResponse
import my.id.jeremia.potholetracker.data.repository.AuthRepository
import my.id.jeremia.potholetracker.data.repository.UserRepository
import my.id.jeremia.potholetracker.ui.base.BaseViewModel
import my.id.jeremia.potholetracker.ui.common.loader.Loader
import my.id.jeremia.potholetracker.ui.common.snackbar.Messenger
import my.id.jeremia.potholetracker.ui.navigation.Destination
import my.id.jeremia.potholetracker.ui.navigation.Navigator
import my.id.jeremia.potholetracker.utils.common.isValidEmail
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    loader: Loader,
    private val authRepository: AuthRepository,
    val navigator: Navigator,
    val messenger: Messenger,
    @ApplicationContext val ctx :Context,
    val userRepository: UserRepository,
) : BaseViewModel(loader,messenger,navigator) {

    companion object {
        const val TAG = "LoginViewModel"
    }

    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _emailError = MutableStateFlow("")
    private val _passwordError = MutableStateFlow("")
    private val _enableLoginButton = MutableStateFlow(true)


    val email = _email.asStateFlow()
    val password = _password.asStateFlow()
    val emailError = _emailError.asStateFlow()
    val passwordError = _passwordError.asStateFlow()
    val enableLoginButton = _enableLoginButton.asStateFlow()

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
            _enableLoginButton.tryEmit(false)
            launchNetwork (
                error={
                    if(it.status != ApiErrorResponse.Status.API_ERROR ){
                        return@launchNetwork;
                    }

                    try{
                        val error = Moshi.Builder()
                            .build()
                            .adapter(AuthLoginErrorResponse::class.java)
                            .fromJson(it.fullResponse)

                        if(error!!.status == "error"){
                            if(error.message == "Wrong email / password"){
                                _emailError.tryEmit(" ")
                                _passwordError.tryEmit("Email atau password yang anda masukkan salah")
                            }

                            if(error!!.reason?.email?.isNotEmpty()!!){
                                _emailError.tryEmit(error.reason!!.email!![0]!!)
                            }
                            if(error!!.reason?.password?.isNotEmpty()!!){
                                _passwordError.tryEmit(error.reason!!.password!![0]!!)
                            }
                        }

                    }catch(_: Exception){}
                },
                finish = {
                    _enableLoginButton.tryEmit(true)
                },
            ){
                _emailError.tryEmit("")
                _passwordError.tryEmit("")

                authRepository.doLogin(email.value, password.value)
                    .collect {
                        userRepository.saveCurrentAuth(
                            Auth(
                                it.data!!.uid!!.toString(),
                                it.data.name!!,
                                it.data.email!!,
                                it.data.accessToken!!
                            )
                        )
                        messenger.deliver(Message.success("Berhasil login"))
                        navigator.navigateTo(Destination.Home.Find.route)
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