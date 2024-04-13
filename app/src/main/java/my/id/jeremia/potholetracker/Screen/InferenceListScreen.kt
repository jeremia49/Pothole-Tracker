package my.id.jeremia.potholetracker.Screen

import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.ViewModel.InferenceListViewModel
import my.id.jeremia.potholetracker.utils.timestampToFormattedString


@Composable
fun InferenceListScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: InferenceListViewModel = viewModel()

    val inferences = viewModel.getAllInferences.collectAsState(initial = listOf())

    BackHandler {
        onBackPressed()
    }

    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = it)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            if(inferences.value.size >= 1){

                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ){
                    IconButton(onClick = {
                        viewModel.deleteAllInference()
                    }) {
                        Icon(
                            painterResource(id = R.drawable.baseline_delete_24),
                            "Bersihkan Histori",
                            modifier = modifier
                                .fillMaxSize()
                        )
                    }
                }

                LazyColumn(
                    modifier = modifier,
                    state= rememberLazyListState()
                ) {
                    items(inferences.value.size) {
                        InferenceItem(
                            id = inferences.value[it].id.toString(),
                            lat = inferences.value[it].latitude.toString(),
                            long = inferences.value[it].longitude.toString(),
                            filename = inferences.value[it].localImagePath,
                            timestamp = timestampToFormattedString(inferences.value[it].timestamp),
                            berlubang = inferences.value[it].status,
                            conf = inferences.value[it].confidence.toString(),
                        )
                    }
                }
            }else{
                Text("Belum ada inference yang dilakukan")
            }

        }
    }




}

@Composable
fun InferenceItem(
    modifier: Modifier = Modifier,
    id:String,
    lat: String,
    long: String,
    filename: String,
    timestamp:String,
    berlubang: String,
    conf:String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Text("ID: ${id}")
        Text("Lat: ${lat}")
        Text("Long: ${long}")
        Text("Timstamp: ${timestamp}")
        Text("Status: ${berlubang}")
        Text("Confidence: ${conf}")
        Image(
            BitmapFactory.decodeFile(filename).asImageBitmap(),
            "Image Bitmap",
            modifier=modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.5f)
                .align(Alignment.CenterHorizontally)

        )
    }
}