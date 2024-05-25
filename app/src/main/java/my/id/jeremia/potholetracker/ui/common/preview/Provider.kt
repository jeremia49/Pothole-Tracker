package my.id.jeremia.potholetracker.ui.common.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import my.id.jeremia.potholetracker.data.local.db.entity.InferenceData
import my.id.jeremia.potholetracker.data.local.db.entity.VerifiedInference
import my.id.jeremia.potholetracker.utils.common.CalendarUtils

class HomeContributeListItemProvider : PreviewParameterProvider<InferenceData> {
    override val values = sequenceOf(
        InferenceData(
            id = 0,
            latitude = 0.0f,
            longitude = 0.0f,
            localImgPath = "",
            status="berlubang",
            createdAt = CalendarUtils.now(),
            createdTimestamp = 0L,
            synced = false,
        ),
    )
}
class HomeListDataItemProvider : PreviewParameterProvider<VerifiedInference> {
    override val values = sequenceOf(
        VerifiedInference(
            id = 0,
            latitude = 0.0f,
            longitude = 0.0f,
            status="berlubang",
            timestamp = 1L,
            remoteImgUrl = "https://google.com"
        ),
    )
}
