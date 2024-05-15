package my.id.jeremia.potholetracker.ui.splash

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.ui.common.image.LottieImage
import my.id.jeremia.potholetracker.ui.common.loader.LoadingPlaceholder

@Composable
fun Splash(modifier: Modifier, viewModel: SplashViewModel) {
    BackHandler { viewModel.navigator.finish() }
    SplashView(modifier)
}

@Composable
private fun SplashView(modifier: Modifier) {
    var visible by remember {
        mutableStateOf(true)
    }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        label = "alpha"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LottieImage(
            modifier
                .size(150.dp)
                .graphicsLayer {
                    alpha = animatedAlpha
                },
            R.raw.triangle_loading,
            true,
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    SplashView(modifier = Modifier)
}

