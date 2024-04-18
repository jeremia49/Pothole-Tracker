package my.id.jeremia.potholetracker.Screen

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.Graph
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme

@Composable
fun HomeScreen(
    navMaps: () -> Unit,
    navCollab: () -> Unit,
    navInferenceList: () -> Unit,
    modifier: Modifier = Modifier
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleScope = lifecycleOwner.lifecycleScope

    val isBackupLoading = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        Column(
            modifier = modifier
                .padding(start = 0.dp, top = 5.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Text(
                "Halo Jeremia,",
                fontSize = 24.sp,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Start,
                modifier = modifier
                    .fillMaxWidth()
            )

            Text(
                "Silahkan pilih task berikut :",
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                modifier = modifier
                    .fillMaxWidth()
            )
        }

        Column(
            modifier = modifier
                .width(500.dp)
                .height(200.dp)
                .fillMaxSize()
                .padding(20.dp)
                .border(width = 1.dp, MaterialTheme.colorScheme.outline)
                .clickable {
                    navMaps()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Icon(
                painterResource(id = R.drawable.baseline_map_24),
                "Home",
                modifier = modifier
                    .size(100.dp)
            )

            Text("Lihat Pemetaan Jalan Rusak")

        }

        Column(
            modifier = modifier
                .width(500.dp)
                .height(200.dp)
                .fillMaxSize()
                .padding(20.dp)
                .border(width = 1.dp, MaterialTheme.colorScheme.outline)
                .clickable {
                    navCollab()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painterResource(id = R.drawable.baseline_photo_camera_back_24),
                "Home",
                modifier = modifier
                    .size(100.dp)
            )
            Text("Kontribusi Pemetaan Jalan Rusak")
        }

        Column(
            modifier = modifier
                .width(500.dp)
                .height(200.dp)
                .fillMaxSize()
                .padding(20.dp)
                .border(width = 1.dp, MaterialTheme.colorScheme.outline)
                .clickable {
                    navInferenceList()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painterResource(id = R.drawable.baseline_photo_camera_back_24),
                "Home",
                modifier = modifier
                    .size(100.dp)
            )
            Text("Lihat Histori Inference")
        }

        if (isBackupLoading.value) {

            CircularProgressIndicator()

        } else {

            ElevatedButton(
                onClick = {
                    lifecycleScope.launch(Dispatchers.IO) {
                        isBackupLoading.value = true
                        delay(1000)
                        if (
                            Graph.backupDatabase(context = context) == 0
                        ) {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(
                                    context,
                                    "Berhasil backup database",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(
                                    context,
                                    "Gagal backup database",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        isBackupLoading.value = false
                    }
                },
                modifier = modifier
                    .width(300.dp)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally),
                enabled = !isBackupLoading.value
            ) {
                Text("Backup Database")
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PotholeTrackerTheme {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .safeContentPadding()
        ) {
            HomeScreen({}, {}, {})
        }
    }
}
