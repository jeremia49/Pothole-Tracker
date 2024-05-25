package my.id.jeremia.potholetracker.ui.HomeContributeList

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
    val inferences: List<InferenceData> = _inferences

    private val _isSyncingGlobal = MutableStateFlow(false)
    val isSyncingGlobal = _isSyncingGlobal.asStateFlow()

    init {
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

    suspend fun setSyncing(inference: InferenceData, status: Boolean): InferenceData {
        //Edit Inference Object
        val editedInference = inference.copy(
            isSyncing = status
        )

        //Edit Database
        localInferenceRepository.updateInference(
            editedInference
        ).first()

        //Edit UI
        var idx = -1;
        for (i in 0 until _inferences.size) {
            if (_inferences[i].id == inference.id) {
                idx = i
            }
        }
        if (idx != -1) {
            _inferences[idx] = inference.copy(
                isSyncing = status
            )
        }
        return editedInference
    }

    suspend fun uploadImage(inference: InferenceData, success: (url: String) -> Unit) {
        try {
            localInferenceRepository.uploadImage(inference.localImgPath)
                .first {
                    val remoteUrl = it.data
                    if (remoteUrl != null) success(remoteUrl)
                    true
                }
        }catch (_:Exception){}
    }

    suspend fun uploadData(inference: InferenceData, success: () -> Unit) {
        try{
            localInferenceRepository.sendInference(inference)
                .first {
                    val data = it
                    if (data.status == "ok" && data.message == "Berhasil") success()
                    true
                }
        }catch(_:Exception){}
    }


    suspend fun uploadImageAndUpdateDatabase(inference: InferenceData): InferenceData {
        var editedInference = inference.copy()
        if (inference.remoteImgPath == "") {
            uploadImage(inference) { url ->
                //Edit Inference Object
                editedInference = editedInference.copy(
                    remoteImgPath = url
                )

                //Edit Database
                viewModelScope.launch{
                    localInferenceRepository.updateInference(
                        editedInference
                    ).first()
                }


                //Edit UI
                var idx = -1;
                for (i in 0 until _inferences.size) {
                    if (_inferences[i].id == inference.id) {
                        idx = i
                    }
                }
                if (idx != -1) {
                    _inferences[idx] = editedInference
                }
            }
        }
        return editedInference
    }

    suspend fun uploadDataAndUpdateDatabase(inference: InferenceData): InferenceData {
        var editedInference = inference.copy()
        if (inference.remoteImgPath != "") {
            uploadData(inference) {
                //Edit Inference Object
                editedInference = editedInference.copy(
                    isSyncing = false,
                    synced = true,
                )

                //Edit Database
                viewModelScope.launch {
                    localInferenceRepository.updateInference(
                        editedInference
                    ).first()
                }

                //Edit UI
                var idx = -1;
                for (i in 0 until _inferences.size) {
                    if (_inferences[i].id == inference.id) {
                        idx = i
                    }
                }
                if (idx != -1) {
                    _inferences[idx] = editedInference
                }
            }
        }
        return editedInference
    }

    fun toggleUpload() {
        _isSyncingGlobal.value = !isSyncingGlobal.value
        if (_isSyncingGlobal.value) upload()
    }


    fun upload() {
        viewModelScope.launch {
            localInferenceRepository.fetchUnsyncedInference()
                .collect {

                    viewModelScope.launch {
                        val dcount = it.size
                        var done = 0
                        for (currentInferenceData in it) {
                            if(!_isSyncingGlobal.value){
                                continue
                            }
                            var updateInference = currentInferenceData.copy()
                            Log.e(TAG, "Current Inference : ${updateInference}")

                            updateInference = setSyncing(updateInference, true)
                            Log.d(TAG, "Syncing : ${updateInference}")

                            updateInference = uploadImageAndUpdateDatabase(updateInference)
                            Log.d(TAG, "image database : ${updateInference}")

                            Log.d(TAG, "upload data : ${updateInference}")
                            updateInference = uploadDataAndUpdateDatabase(updateInference)
                            Log.d(TAG, "upload data : ${updateInference}")

                            updateInference = setSyncing(updateInference, false)
                            Log.d(TAG, "syncing off :  ${updateInference}")
                            done += 1
                        }
                        if(dcount == done ){
                            messenger.deliver(Message.success("Upload Success"))
                            toggleUpload()
                        }
                    }
                }


        }
    }


    fun resetTable() {
        runBlocking {
            localInferenceRepository.resetTable()
                .first()
        }

    }


}