package my.id.jeremia.potholetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import my.id.jeremia.potholetracker.ui.login.Login
import my.id.jeremia.potholetracker.ui.login.LoginViewModel
import my.id.jeremia.potholetracker.ui.register.Register
import my.id.jeremia.potholetracker.ui.register.RegisterViewModel
import my.id.jeremia.potholetracker.ui.splash.Splash
import my.id.jeremia.potholetracker.ui.splash.SplashViewModel

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Destination.Splash.route,
    navigator: Navigator,
    finish: () -> Unit = {},
) {
    NavHandler(
        navController = navController,
        navigator = navigator,
        finish = finish
    )


    NavHost(
        navController = navController,
        startDestination = startDestination,
    )
    {

        composable(Destination.Splash.route) {
            val viewModel: SplashViewModel = hiltViewModel(key = SplashViewModel.TAG)
            Splash(modifier = modifier, viewModel=viewModel)
        }

        composable(Destination.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel(key = LoginViewModel.TAG)
            Login(modifier=modifier, viewModel = viewModel)
        }

        composable(Destination.Register.route){
            val viewModel: RegisterViewModel = hiltViewModel(key = RegisterViewModel.TAG)
            Register(modifier=modifier, viewModel = viewModel)
        }


    }


}