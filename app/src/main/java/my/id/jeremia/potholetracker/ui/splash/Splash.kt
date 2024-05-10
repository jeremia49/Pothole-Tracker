package my.id.jeremia.potholetracker.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import my.id.jeremia.potholetracker.R

@Composable
fun Splash(modifier: Modifier) {
//    BackHandler { viewModel.navigator.finish() }
    SplashView(modifier)
}

@Composable
private fun SplashView(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(300.dp),
            painter = painterResource(R.drawable.pothole_logolabel),
            contentDescription = "Logo"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    SplashView(modifier = Modifier)
}

