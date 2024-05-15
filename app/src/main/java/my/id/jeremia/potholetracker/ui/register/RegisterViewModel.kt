package my.id.jeremia.potholetracker.ui.register

import android.content.Context
import androidx.datastore.preferences.protobuf.Api
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthLoginErrorResponse
import my.id.jeremia.potholetracker.data.remote.apis.auth.response.AuthRegisterErrorResponse
import my.id.jeremia.potholetracker.data.remote.response.ApiErrorResponse
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
) : BaseViewModel(loader, messenger, navigator) {

    companion object {
        const val TAG = "RegisterViewModel"
    }

    private val _name = MutableStateFlow("")
    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _passwordConfirm = MutableStateFlow("")
    private val _enableRegisterButton = MutableStateFlow(true)

    private val _nameError = MutableStateFlow("")
    private val _emailError = MutableStateFlow("")
    private val _passwordError = MutableStateFlow("")
    private val _passwordConfirmError = MutableStateFlow("")

    val name = _name.asStateFlow()
    val email = _email.asStateFlow()
    val password = _password.asStateFlow()
    val passwordConfirm = _passwordConfirm.asStateFlow()
    val enableRegisterButton = _enableRegisterButton.asStateFlow()

    val nameError = _nameError.asStateFlow()
    val emailError = _emailError.asStateFlow()
    val passwordError = _passwordError.asStateFlow()
    val passwordConfirmError = _passwordConfirmError.asStateFlow()

    fun onNameChange(input: String) {
        _name.tryEmit(input)
        if (nameError.value.isNotEmpty()) _nameError.tryEmit("")
    }

    fun onEmailChange(input: String) {
        _email.tryEmit(input)
        if (emailError.value.isNotEmpty()) _emailError.tryEmit("")
    }

    fun onPasswordChange(input: String) {
        _password.tryEmit(input)
        if (passwordError.value.isNotEmpty()) _passwordError.tryEmit("")
    }

    fun onPasswordConfirmationChange(input: String) {
        _passwordConfirm.tryEmit(input)
        if (passwordConfirmError.value.isNotEmpty()) _passwordConfirmError.tryEmit("")
    }

    fun doRegister() {
        if (validate()) {
            _enableRegisterButton.tryEmit(false)
            launchNetwork(
                error = {
                    if(it.status != ApiErrorResponse.Status.API_ERROR ){
                        return@launchNetwork;
                    }

                    try{
                        val error = Moshi.Builder()
                            .build()
                            .adapter(AuthRegisterErrorResponse::class.java)
                            .fromJson(it.fullResponse)

                        if(error!!.status == "error"){
                            if(error.reason?.name != null){
                                _nameError.tryEmit(error.reason.name[0]!!)
                            }
                            if(error.reason?.email != null){
                                _emailError.tryEmit(error.reason.email[0]!!)
                            }
                            if(error.reason?.password != null ){
                                _passwordError.tryEmit(error.reason.password[0]!!)
                            }
                        }

                    }catch(_: Exception){}
                },
                finish = {
                    _enableRegisterButton.tryEmit(true)
                },
            ) {
                authRepository.doRegister(email.value, name.value,password.value)
                    .collect {
                        messenger.deliver(Message.success("Berhasil membuat akun, silahkan login kembali"))
                        navigator.navigateTo(Destination.Login.route)
                    }
            }

        }

    }

    fun navLogin() {
        navigator.navigateTo(Destination.Login.route)
    }

    private fun validate(): Boolean {
        var error = false
        if (!email.value.isValidEmail()) _emailError.tryEmit("Invalid Email").run { error = true }
        if (password.value.length < 8) _passwordError.tryEmit("Password length should be at least 8")
            .run { error = true }
        if (name.value.isEmpty()) _nameError.tryEmit("Nama tidak boleh kosong")
            .run { error = true }
        if (password.value != passwordConfirm.value) _passwordConfirmError.tryEmit("Password dan Password Konfirmasi berbeda")
            .run { error = true }
        return !error
    }

}