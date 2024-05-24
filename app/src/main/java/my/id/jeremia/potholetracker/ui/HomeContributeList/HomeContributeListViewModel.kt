package my.id.jeremia.potholetracker.ui.HomeContributeList

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.data.local.db.entity.InferenceData
import my.id.jeremia.potholetracker.data.repository.LocalInferenceRepository
import my.id.jeremia.potholetracker.ui.base.BaseViewModel
import my.id.jeremia.potholetracker.ui.common.loader.Loader
import my.id.jeremia.potholetracker.ui.common.snackbar.Messenger
import my.id.jeremia.potholetracker.ui.navigation.Navigator
import javax.inject.Inject

@HiltViewModel
class HomeContributeListViewModel @Inject constructor(
    val navigator: Navigator,
    val messenger: Messenger,
    val loader: Loader,
    val localInferenceRepository: LocalInferenceRepository,
) : BaseViewModel(
    loader,
    messenger,
    navigator,
) {

    companion object {
        const val TAG = "HomeContributeListViewModel"
    }

    private val pageItemCount = 10
    private var loading = false
    private var currentPageNumber = 1

    private val _inferences = mutableStateListOf<InferenceData>()
    val inferences : List<InferenceData> = _inferences

    init{
        loadInferences(currentPageNumber)
    }

    private fun loadInferences(pageNumber: Int) {
        if (loading) return
        loading = true
        viewModelScope.launch {
            localInferenceRepository
                .fetchInferences(pageNumber, pageItemCount)
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

    fun loadMore() {
        loadInferences(currentPageNumber)
    }


    fun delete(inference: InferenceData) {
        viewModelScope.launch {
            localInferenceRepository
                .deleteInference(inference)
                .catch {
                    messenger.deliverRes(Message.error(R.string.something_went_wrong))
                }
                .collect {
                    _inferences.remove(inference)
                    messenger.deliverRes(Message.success(R.string.delete_inference_success))
                }
        }
    }

    fun select(inference: InferenceData) {
        // TODO
    }
}