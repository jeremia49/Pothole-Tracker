package my.id.jeremia.potholetracker.ui.HomeListData


import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.data.local.db.entity.InferenceData
import my.id.jeremia.potholetracker.data.local.db.entity.VerifiedInference
import my.id.jeremia.potholetracker.ui.HomeMyAccount.HomeMyAccountViewModel
import my.id.jeremia.potholetracker.ui.common.image.NetworkImage
import my.id.jeremia.potholetracker.ui.common.list.InfiniteLazyColumn
import my.id.jeremia.potholetracker.ui.common.preview.HomeContributeListItemProvider
import my.id.jeremia.potholetracker.ui.common.preview.HomeListDataItemProvider
import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme
import my.id.jeremia.potholetracker.ui.theme.grey
import my.id.jeremia.potholetracker.ui.theme.manropeFontFamily
import my.id.jeremia.potholetracker.utils.common.CalendarUtils


@Composable
fun HomeListData(modifier: Modifier = Modifier, viewModel: HomeListDataViewModel) {
    BackHandler { viewModel.navigator.finish() }
    HomeListDataView(
        modifier,
        inferences = viewModel.inferences,
        loadMore = {viewModel.loadMore()},
        select = {viewModel.select()},
        toggleSync = {viewModel.toggleSync()},
    )
}


@Composable
fun HomeListDataView(
    modifier: Modifier = Modifier,
    inferences: List<VerifiedInference> = emptyList(),
    loadMore: () -> Unit,
    select: (InferenceData) -> Unit,
    toggleSync: () -> Unit = {},
    isSyncing: Boolean = false,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column {
            Text(
                "List Inferensi",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
            InfiniteLazyColumn(loadMore = loadMore, modifier = Modifier.fillMaxSize(), content = {
                item(
                    key = "bar"
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {


                        OutlinedButton(onClick = {
                            toggleSync()
                        }) {
                            if (isSyncing) {
                                Icon(
                                    Icons.Filled.Close, "Cancel", modifier = Modifier
                                )
                                Text(
                                    "Batalkan",
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(5.dp),
                                )
                            } else {
                                Icon(
                                    Icons.Filled.ArrowCircleUp, "Upload", modifier = Modifier
                                )
                                Text(
                                    "Sinkronikasi",
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(5.dp),
                                )
                            }
                        }
                    }
                }
                items(inferences) { inference ->
                    HomeListDataItem(
                        inference = inference, select = select
                    )
                    HorizontalDivider()
                }
            })
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeListDataItem(
    inference: VerifiedInference, select: (InferenceData) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

    ) {
        Column(
            Modifier
                .height(IntrinsicSize.Min)
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Latitude: ${inference.latitude}",
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Longitude: ${inference.longitude}",
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Status : ${inference.status}",
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Waktu : ${inference.timestamp}",
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "remoteUrl : ${inference.remoteImgUrl}",
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Box(
            modifier = Modifier.size(150.dp), contentAlignment = Alignment.Center
        ) {
            NetworkImage(
                inference.remoteImgUrl,
                contentDescription = "Verified Inference",
                contentScale = ContentScale.Fit,
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeListDataItemPreview(
    @PreviewParameter(HomeListDataItemProvider::class, limit = 1) inference: VerifiedInference
) {
    PotholeTrackerTheme {
        HomeListDataItem(inference = inference, select = {})
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeFindPreview(
    @PreviewParameter(HomeListDataItemProvider::class, limit = 1) inference: VerifiedInference
) {
    HomeListDataView(
        loadMore = {},
        select = {},
        inferences = listOf(inference),
    )
}