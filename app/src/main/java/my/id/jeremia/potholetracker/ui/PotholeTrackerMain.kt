package my.id.jeremia.potholetracker.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import my.id.jeremia.potholetracker.ui.common.loader.Loader
import my.id.jeremia.potholetracker.ui.common.loader.Loading
import my.id.jeremia.potholetracker.ui.common.snackbar.AppSnackbar
import my.id.jeremia.potholetracker.ui.common.snackbar.Messenger
import my.id.jeremia.potholetracker.ui.navigation.NavGraph
import my.id.jeremia.potholetracker.ui.navigation.Navigator
import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme

@Composable
fun PotholeTrackerMain(
    navigator: Navigator,
    loader: Loader,
    messenger: Messenger,
    finish: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    PotholeTrackerTheme {
        val navController = rememberNavController()
        Scaffold(
            modifier = Modifier.imePadding(),
            snackbarHost = { AppSnackbar(snackbarHostState, messenger) },
            // it will render bottom bar only in the home route
//            bottomBar = { HomeBottomBar(navController = navController) },
        ) { innerPaddingModifier ->
            NavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPaddingModifier),
                navigator = navigator,
                finish = finish
            )
            Loading(
                modifier = Modifier
                    .padding(innerPaddingModifier)
                    .fillMaxWidth(),
                loader = loader
            )
        }
    }
}