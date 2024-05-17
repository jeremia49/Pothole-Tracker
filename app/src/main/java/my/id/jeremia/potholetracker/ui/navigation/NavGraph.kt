package my.id.jeremia.potholetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import my.id.jeremia.potholetracker.ui.HomeContribute.HomeContribute
import my.id.jeremia.potholetracker.ui.HomeContribute.HomeContributeViewModel
import my.id.jeremia.potholetracker.ui.HomeFind.HomeFind
import my.id.jeremia.potholetracker.ui.HomeFind.HomeFindViewModel
import my.id.jeremia.potholetracker.ui.HomeListData.HomeListData
import my.id.jeremia.potholetracker.ui.HomeListData.HomeListDataView
import my.id.jeremia.potholetracker.ui.HomeListData.HomeListDataViewModel
import my.id.jeremia.potholetracker.ui.HomeMyAccount.HomeMyAccount
import my.id.jeremia.potholetracker.ui.HomeMyAccount.HomeMyAccountViewModel
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
            Splash(modifier = modifier, viewModel = viewModel)
        }

        composable(Destination.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel(key = LoginViewModel.TAG)
            Login(modifier = modifier, viewModel = viewModel)
        }

        composable(Destination.Register.route) {
            val viewModel: RegisterViewModel = hiltViewModel(key = RegisterViewModel.TAG)
            Register(modifier = modifier, viewModel = viewModel)
        }

        navigation(
            route = Destination.Home.route,
            startDestination = Destination.Home.Find.route
        ) {

            composable(Destination.Home.Find.route){
                val viewModel : HomeFindViewModel = hiltViewModel(key=HomeFindViewModel.TAG)
                HomeFind(modifier,viewModel)
            }

            composable(Destination.Home.Data.route){
                val viewModel : HomeListDataViewModel = hiltViewModel(key=HomeListDataViewModel.TAG)
                HomeListData(modifier,viewModel)
            }

            composable(Destination.Home.Contribute.route){
                val viewModel : HomeContributeViewModel = hiltViewModel(key=HomeContributeViewModel.TAG)
                HomeContribute(modifier,viewModel)
            }

            composable(Destination.Home.MyAccount.route){
                val viewModel : HomeMyAccountViewModel = hiltViewModel(key=HomeMyAccountViewModel.TAG)
                HomeMyAccount(modifier,viewModel)
            }


        }


    }


}