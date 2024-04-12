package my.id.jeremia.potholetracker.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.Data.InferenceData
import my.id.jeremia.potholetracker.Data.InferenceRepository
import my.id.jeremia.potholetracker.Graph
import my.id.jeremia.potholetracker.Graph.inferenceRepository

class InferenceListViewModel(
    inferenceRepository: InferenceRepository = Graph.inferenceRepository
):ViewModel() {

    lateinit var getAllInferences : Flow<List<InferenceData>>
    init{
        viewModelScope.launch{
            getAllInferences = inferenceRepository.getAllInferences()
        }
    }

    fun addInference(inferenceData: InferenceData){
        viewModelScope.launch(Dispatchers.IO){
            inferenceRepository.addAnInference(inferenceData)
        }
    }

    fun updateInference(inferenceData: InferenceData){
        viewModelScope.launch(Dispatchers.IO){
            inferenceRepository.updateAnInference(inferenceData)
        }
    }

    fun getInferenceById(id:Long): Flow<InferenceData> {
        return inferenceRepository.getAnInferenceById(id)
    }

    fun deleteInference(inferenceData: InferenceData){
        viewModelScope.launch(Dispatchers.IO){
            inferenceRepository.deleteAnInference(inferenceData)
        }
    }

    fun deleteAllInference(){
        viewModelScope.launch(Dispatchers.IO){
            inferenceRepository.deleteAllInferences()
        }
    }

}