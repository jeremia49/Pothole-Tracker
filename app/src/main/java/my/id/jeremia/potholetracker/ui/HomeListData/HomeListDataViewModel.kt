package my.id.jeremia.potholetracker.ui.HomeListData

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.data.local.db.entity.InferenceData
import my.id.jeremia.potholetracker.data.local.db.entity.VerifiedInference
import my.id.jeremia.potholetracker.data.repository.VerifiedInferenceRepository
import my.id.jeremia.potholetracker.ui.base.BaseViewModel
import my.id.jeremia.potholetracker.ui.common.loader.Loader
import my.id.jeremia.potholetracker.ui.common.snackbar.Messenger
import my.id.jeremia.potholetracker.ui.navigation.Navigator
import javax.inject.Inject

@HiltViewModel
class HomeListDataViewModel @Inject constructor(
    private val verifiedInferenceRepository: VerifiedInferenceRepository,
    val navigator: Navigator,
    val messenger: Messenger,
    val loader: Loader,
) : BaseViewModel(
    loader,
    messenger,
    navigator,
) {

    companion object {
        const val TAG = "HomeListDataViewModel"
    }

    private val pageItemCount = 10
    private var loading = false
    private var currentPageNumber = 1

    private val _inferences = mutableStateListOf<VerifiedInference>()
    val inferences: List<VerifiedInference> = _inferences

//    private val _isSyncingGlobal = MutableStateFlow(false)
//    val isSyncingGlobal = _isSyncingGlobal.asStateFlow()

    init {
        loadInferences(currentPageNumber)
    }

    fun loadMore() {
        loadInferences(currentPageNumber)
    }

    private fun loadInferences(pageNumber: Int) {
        if (loading) return
        loading = true
        viewModelScope.launch {
            verifiedInferenceRepository
                .fetchPaginatedInferences(pageNumber, pageItemCount)
                .catch {
                    loading = false
                    messenger.deliverRes(Message.error(R.string.something_went_wrong))
                }
                .collect {
                    if (it.isNotEmpty()) {
                        _inferences.addAll(it)
                        currentPageNumber++
                        loading = false
                    }
                }
        }
    }

    fun toggleSync() {
        fetchFromNetwork()
    }

    fun fetchFromNetwork() {
        launchNetwork {
            val data = verifiedInferenceRepository
                .syncFromServer()
                .first()

            if (data.data != null) {
                verifiedInferenceRepository
                    .resetTable()
                    .first()

                _inferences.clear()
                for (item in data.data) {
                    val verifiedInferenceItem = VerifiedInference(
                        longitude = item!!.longitude!!.toFloat(),
                        latitude = item.latitude!!.toFloat(),
                        status = item.status!!,
                        remoteImgUrl = item.url!!,
                        timestamp = item.timestamp!!.toLong(),
                    )

                    verifiedInferenceRepository
                        .insertVerifiedInference(verifiedInferenceItem)

                    _inferences.add(verifiedInferenceItem)

                }
                verifiedInferenceRepository.setVerifiedInferenceUpdateTime(System.currentTimeMillis())
            }
        }
    }

    fun select() {

    }


}