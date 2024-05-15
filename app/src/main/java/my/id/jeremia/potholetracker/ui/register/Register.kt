package my.id.jeremia.potholetracker.ui.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.ui.login.LoginView
import my.id.jeremia.potholetracker.ui.login.LoginViewModel
import my.id.jeremia.potholetracker.ui.theme.manropeFontFamily

@Composable
fun Register(modifier: Modifier = Modifier, viewModel: RegisterViewModel) {
    BackHandler { viewModel.navigator.finish() }
    RegisterView(
        modifier,
        name=viewModel.name.collectAsStateWithLifecycle().value,
        email=viewModel.email.collectAsStateWithLifecycle().value,
        password=viewModel.password.collectAsStateWithLifecycle().value,
        passwordConfirm=viewModel.passwordConfirm.collectAsStateWithLifecycle().value,
        nameError=viewModel.nameError.collectAsStateWithLifecycle().value,
        emailError=viewModel.emailError.collectAsStateWithLifecycle().value,
        passwordError=viewModel.passwordError.collectAsStateWithLifecycle().value,
        passwordConfirmError=viewModel.passwordConfirmError.collectAsStateWithLifecycle().value,
        enableRegisterButton=viewModel.enableRegisterButton.collectAsStateWithLifecycle().value,
        onNameChange={viewModel.onNameChange(it)},
        onEmailChange= {viewModel.onEmailChange(it)},
        onPasswordChange={viewModel.onPasswordChange(it)},
        onPasswordConfirmChange={viewModel.onPasswordConfirmationChange(it)},
        register={viewModel.doRegister()},
        navLogin={viewModel.navLogin()},
    )
}


@Composable
fun RegisterView(
    modifier: Modifier = Modifier,
    name:String,
    email: String,
    password: String,
    passwordConfirm:String,
    nameError:String,
    emailError: String,
    passwordError: String,
    passwordConfirmError:String,
    enableRegisterButton:Boolean,
    onNameChange:(String)->Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String)->Unit,
    register: () -> Unit,
    navLogin:()->Unit,
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {


        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceEvenly,

        ) {


            Column(
                modifier = modifier
                    .padding(
                        top = 10.dp
                    )
            ) {

                Text(
                    stringResource(R.string.register_header),
                    fontFamily = manropeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp,
//                    color = Color(0xFF000000),
                )
                Text(
                    stringResource(R.string.register_subheader),
                    fontFamily = manropeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
//                    color = Color(0xFF999EA1),
                )
            }

            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 5.dp,
                    bottom = 4.dp
                )
            ) {
                Text(
                    "Nama Lengkap",
                    fontFamily = manropeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    value = name,
                    onValueChange = onNameChange,
                    placeholder = { Text("Nama lengkap anda") },
                    singleLine = true,
                    isError = nameError.isNotEmpty(),
                    supportingText = {
                        Text(text = nameError)
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                )

                Text(
                    "Email",
                    fontFamily = manropeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    value = email,
                    onValueChange = onEmailChange,
                    placeholder = { Text("Email anda") },
                    singleLine = true,
                    isError = emailError.isNotEmpty(),
                    supportingText = {
                        Text(text = emailError)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                )

                Text(
                    "Password",
                    fontFamily = manropeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    value = password,
                    onValueChange = onPasswordChange,
                    placeholder = { Text("Password") },
                    singleLine = true,
                    isError = passwordError.isNotEmpty(),
                    supportingText = {
                        Text(text = passwordError)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                )

                Text(
                    "Password Konfirmasi",
                    fontFamily = manropeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    value = passwordConfirm,
                    onValueChange = onPasswordConfirmChange,
                    placeholder = { Text("Password Konfirmasi") },
                    singleLine = true,
                    isError = passwordConfirmError.isNotEmpty(),
                    supportingText = {
                        Text(text = passwordConfirmError)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 15.dp,
                            bottom = 20.dp
                        ),
                    onClick = register,
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = Color.Black
                    ),
                    enabled = enableRegisterButton
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(R.string.daftar),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White,
                            fontFamily = manropeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp,
                        )
                    )
                }

            }

            Row(
                modifier= modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ){
                Text("Sudah ada akun ? ",
                    style = TextStyle(
//                        color = grey,
                        fontFamily = manropeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                )
                TextButton(onClick = navLogin) {
                    Text("Login",
                        style = TextStyle(
//                            color = Color.Black,
                            fontFamily = manropeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                        )
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    RegisterView(
        email = "",
        password = "",
        emailError = "",
        passwordError = "",
        onEmailChange = {},
        onPasswordChange = {},
        name = "",
        onNameChange = {},
        nameError = "",
        passwordConfirm = "",
        passwordConfirmError = "",
        onPasswordConfirmChange = {},
        navLogin = {},
        register = {},
        enableRegisterButton = true,
    )
}