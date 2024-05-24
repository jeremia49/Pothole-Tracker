package my.id.jeremia.potholetracker.ui.HomeContribute

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.ui.HomeContribute.ContributeActivity.ContributeActivity
import my.id.jeremia.potholetracker.ui.navigation.Destination
import my.id.jeremia.potholetracker.ui.theme.manropeFontFamily

@Composable
fun HomeContribute(modifier: Modifier = Modifier, viewModel: HomeContributeViewModel) {
    BackHandler { viewModel.navigator.finish() }
    HomeContributeView(
        modifier,
        viewModel
    )
}


@Composable
fun HomeContributeView(
    modifier: Modifier = Modifier,
    viewModel: HomeContributeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .systemBarsPadding()
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Kontribusi",
                fontFamily = manropeFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp),
            )

            Text(
                stringResource(R.string.notice_contribute),
                fontFamily = manropeFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(25.dp)
            )

            if (!viewModel.cameraPermission.value || !viewModel.locationPermission.value) {
                AskPermissionView(modifier = Modifier, viewModel)
            } else {
                OutlinedButton(
                    onClick = {
                        context.startActivity(Intent(context, ContributeActivity::class.java))
                    },
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                ) {
                    Text("Mulai Kontribusi")
                }

                OutlinedButton(onClick = {
                    viewModel.navigator.navigateTo(Destination.Home.ContributeList.route)
                }) {
                    Text("Lihat Histori Kontribusi")
                }
            }

        }
    }


}

@Preview(showBackground = true)
@Composable
private fun HomeContributePreview() {
    HomeContributeView(

    )
}