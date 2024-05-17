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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.ui.common.image.NetworkImage
import my.id.jeremia.potholetracker.utils.crypto.hashSHA256

@Composable
fun HomeMyAccount(modifier: Modifier = Modifier, viewModel: HomeMyAccountViewModel) {
    BackHandler { viewModel.navigator.finish() }
    HomeMyAccountView(
        modifier,
        doLogout = {viewModel.doLogout()},
        username = viewModel.username.collectAsState(initial = "").value ?: "",
        email = viewModel.email.collectAsState(initial = "").value ?: "",
    )
}


@Composable
fun HomeMyAccountView(
    modifier: Modifier = Modifier,
    doLogout: () -> Unit = {},
    username:String ="Jeremia Manurung",
    email:String="jeremiaman49@gmail.com",
) {
    val emailHash = remember(email){
        mutableStateOf(hashSHA256(email.lowercase()))
    }

    Column(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {

//            Image(
//                painter = painterResource(id = R.drawable.account_circle),
//                "UserAccountLogo",
//                modifier = Modifier
//                    .size(150.dp),
//            )

            NetworkImage(
                url = "https://www.gravatar.com/avatar/${emailHash.value}",
                contentDescription = "User Avatar",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 15.dp),
            )

            Text(username)
            Text("Email: ${email}")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        doLogout();
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = "Logout",
                    modifier = Modifier
                        .padding(start = 30.dp, end = 25.dp)
                        .size(30.dp)

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