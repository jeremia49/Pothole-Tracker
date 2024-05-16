package my.id.jeremia.potholetracker.ui.HomeMyAccount

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import my.id.jeremia.potholetracker.R

@Composable
fun HomeMyAccount(modifier: Modifier = Modifier, viewModel: HomeMyAccountViewModel) {
    BackHandler { viewModel.navigator.finish() }
    HomeMyAccountView(
        modifier,
    )
}


@Composable
fun HomeMyAccountView(
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {

            Image(
                painter = painterResource(id = R.drawable.account_circle),
                "UserAccountLogo",
                modifier = modifier
                    .width(150.dp)
                    .height(150.dp)
                    .padding(top=50.dp, bottom = 0.dp)
                    .border(1.dp,Color.Black)
            )

            Text("Jeremia Manurung")

            Text("Email: jeremiamanurungganteng@gmail.com")

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 10.dp)
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                               println("Keluar")
                    },
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Image(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = "Logout",
                    modifier=Modifier
                        .size(50.dp)
                        .padding(end = 25.dp)
                )
                Text("Keluar")
            }


        }


    }
}

@Preview(showBackground = true)
@Composable
private fun HomeMyAccountPreview() {
    HomeMyAccountView(

    )
}