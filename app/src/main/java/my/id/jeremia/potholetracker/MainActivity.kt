package my.id.jeremia.potholetracker

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import my.id.jeremia.potholetracker.Screen.CollabScreen
import my.id.jeremia.potholetracker.Screen.HomeScreen
import my.id.jeremia.potholetracker.Screen.InferenceListScreen
import my.id.jeremia.potholetracker.Screen.MapScreen
import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContent {
            PotholeTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                {
                    navController.navigate("map")
                },
                {
                    navController.navigate("collab")
                },
                {
                    navController.navigate("inferenceList")
                },
            )
        }
        composable("map") {
            MapScreen()
        }
        composable("collab") {
            CollabScreen({
                navController.popBackStack()
            })
        }
        composable("inferenceList") {
            InferenceListScreen({
                navController.popBackStack()
            })
        }

    }

}
