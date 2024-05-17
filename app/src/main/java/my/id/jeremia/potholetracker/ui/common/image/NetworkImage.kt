package my.id.jeremia.potholetracker.ui.common.image

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import my.id.jeremia.potholetracker.R

@Composable
fun NetworkImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    @DrawableRes errorRes: Int = R.drawable.image_not_available,
    @DrawableRes fallbackRes: Int = R.drawable.image_not_available,
    @DrawableRes placeholderRes: Int = R.drawable.image_not_available
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        placeholder = painterResource(placeholderRes),
        error = painterResource(errorRes),
        fallback = painterResource(fallbackRes),
        modifier = modifier,
        contentScale = contentScale
    )
}