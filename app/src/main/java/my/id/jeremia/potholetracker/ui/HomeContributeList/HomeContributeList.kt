package my.id.jeremia.potholetracker.ui.HomeContributeList

import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.id.jeremia.potholetracker.PotholeTrackerApp
import my.id.jeremia.potholetracker.data.local.db.entity.InferenceData
import my.id.jeremia.potholetracker.ui.common.list.InfiniteLazyColumn
import my.id.jeremia.potholetracker.ui.common.preview.HomeContributeListItemProvider
import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme
import my.id.jeremia.potholetracker.utils.common.CalendarUtils

@Composable
fun HomeContributeList(modifier: Modifier = Modifier, viewModel: HomeContributeListViewModel) {
    HomeContributeListView(
        modifier = modifier,
        inferences = viewModel.inferences,
        loadMore = { viewModel.loadMore() },
        delete = { viewModel.delete(it) },
        select = { viewModel.select(it) },
        upload = { viewModel.upload() }
    )
}

@Composable
private fun HomeContributeListView(
    modifier: Modifier,
    inferences: List<InferenceData>,
    loadMore: () -> Unit,
    delete: (InferenceData) -> Unit,
    select: (InferenceData) -> Unit,
    upload: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .systemBarsPadding()
    ) {
        InfiniteLazyColumn(
            loadMore = loadMore,
            modifier = Modifier
                .fillMaxSize(),
            content = {
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
                            upload()
                        }) {
                            Icon(
                                Icons.Filled.ArrowCircleUp,
                                "Upload",
                                modifier = Modifier
                            )
                            Text(
                                "Upload ke server",
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .padding(5.dp),
                            )
                        }
                    }
                    Text(
                        "Histori Inferensi",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                }
                items(inferences, key = { it.id }) { inference ->
                    HomeContributeListItem(
                        inference = inference,
                        delete = delete,
                        select = select
                    )
                    HorizontalDivider()
                }
            },
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeContributeListPreview(
    @PreviewParameter(HomeContributeListItemProvider::class, limit = 1) inference: InferenceData
) {
    PotholeTrackerTheme {
        HomeContributeListView(
            modifier = Modifier,
            loadMore = {},
            delete = {},
            select = {},
            inferences = listOf(
                inference.copy(id = 1),
                inference.copy(id = 2),
                inference.copy(id = 3),
                inference.copy(id = 4),
                inference.copy(id = 5),
            ),
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeContributeListItem(
    inference: InferenceData,
    delete: (InferenceData) -> Unit,
    select: (InferenceData) -> Unit
) {
    val isDelete = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { select(inference) },
                onLongClick = { isDelete.value = !isDelete.value }
            )
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
                text = "Waktu : ${CalendarUtils.getFormattedDate(inference.createdAt)} ${
                    CalendarUtils.getFormattedTimeSecond(
                        inference.createdAt
                    )
                }",
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = if (inference.synced) "Synced" else "Not Synced",
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Box(
            modifier = Modifier
                .size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                BitmapFactory.decodeFile(inference.localImgPath).asImageBitmap(),
                contentDescription = "Inferenced Image"
            )

//            Surface(
//                shape = RoundedCornerShape(6.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.CalendarMonth,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.inversePrimary,
//                    modifier = Modifier
//                        .wrapContentSize()
//                        .size(42.dp)
//                )
//            }
            if (isDelete.value) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = .85f),
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { delete(inference) },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.DeleteForever,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.inversePrimary,
                        modifier = Modifier
                            .wrapContentSize()
                            .size(42.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeContributeListItemPreview(
    @PreviewParameter(HomeContributeListItemProvider::class, limit = 1) inference: InferenceData
) {
    PotholeTrackerTheme {
        HomeContributeListItem(
            inference = inference,
            delete = {},
            select = {}
        )
    }
}