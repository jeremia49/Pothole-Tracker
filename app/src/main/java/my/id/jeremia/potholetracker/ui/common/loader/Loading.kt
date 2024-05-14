package my.id.jeremia.potholetracker.ui.common.loader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.ui.common.image.LottieImage

@Composable
fun Loading(
    modifier: Modifier = Modifier,
    loader: Loader
) {
    val loading = loader.loading.collectAsStateWithLifecycle().value
    LoadingView(modifier, loading)
}

@Composable
private fun LoadingView(
    modifier: Modifier = Modifier,
    loading: Boolean
) {
    if (!loading) return
    LinearProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colorScheme.tertiary,
        trackColor = MaterialTheme.colorScheme.onPrimary,
    )
}

@Composable
fun LoadingPlaceholder(
    modifier: Modifier = Modifier,
    loading: Boolean
) {
    if (!loading) return
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LottieImage(
            modifier,
             R.raw.triangle_loading,
            true,
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingPlaceholderPreview() {
    LoadingPlaceholder(loading = true)
}

@Preview(showBackground = true)
@Composable
private fun LoadingPreview() {
    LoadingView(loading = true)
}