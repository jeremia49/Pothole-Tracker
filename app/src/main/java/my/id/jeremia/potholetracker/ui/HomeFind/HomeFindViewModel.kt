package my.id.jeremia.potholetracker.ui.HomeFind

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.data.local.datastore.UserDataStore
import my.id.jeremia.potholetracker.data.local.db.entity.VerifiedInference
import my.id.jeremia.potholetracker.data.repository.VerifiedInferenceRepository
import my.id.jeremia.potholetracker.ui.base.BaseViewModel
import my.id.jeremia.potholetracker.ui.common.loader.Loader
import my.id.jeremia.potholetracker.ui.common.snackbar.Messenger
import my.id.jeremia.potholetracker.ui.navigation.Navigator
import javax.inject.Inject


@HiltViewModel()
class HomeFindViewModel @Inject constructor(
    val navigator: Navigator,
    val messenger: Messenger,
    val loader: Loader,
    private val verifiedInferenceRepository: VerifiedInferenceRepository,
) : BaseViewModel(
    loader,
    messenger,
    navigator,
) {

    companion object {
        const val TAG = "HomeFindViewModel"
    }

    private val _inferences = mutableStateListOf<VerifiedInference>()
    val inferences: List<VerifiedInference> = _inferences

    init {
        viewModelScope.launch {
            verifiedInferenceRepository
                .fetchAll()
                .catch {
                    messenger.deliverRes(Message.error(R.string.something_went_wrong))
                }
                .collect {
                    if (it.isNotEmpty()) {
                        _inferences.addAll(it)
                    }
                }
        }
    }

}