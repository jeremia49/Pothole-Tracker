package my.id.jeremia.potholetracker.Screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme

@Composable
fun HomeScreen(navMaps:()->Unit, navCollab:()->Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start,

        ) {

        Column(
            modifier = modifier
                .padding(start = 0.dp, top = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Text(
                "Halo Jeremia,",
                fontSize = 24.sp,
                fontFamily = FontFamily.Serif,
            )

            Text(
                "Silahkan pilih task berikut :",
                fontSize = 16.sp,
            )
        }

        Column(
            modifier = modifier
                .weight(1f)
                .fillMaxSize()
                .padding(20.dp)
                .border(width = 1.dp, Color.Black)
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
                .weight(1f)
                .fillMaxSize()
                .padding(20.dp)
                .border(width = 1.dp, Color.Black)
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
            HomeScreen({},{})
        }
    }
}
