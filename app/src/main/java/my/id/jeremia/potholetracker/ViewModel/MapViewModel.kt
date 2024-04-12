package my.id.jeremia.potholetracker.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.Data.InferenceData
import my.id.jeremia.potholetracker.Data.InferenceRepository
import my.id.jeremia.potholetracker.Graph

class MapViewModel(
    inferenceRepository: InferenceRepository=Graph.inferenceRepository
) : ViewModel() {


    lateinit var getAllInferences : Flow<List<InferenceData>>
    init{
        viewModelScope.launch{
            getAllInferences = inferenceRepository.getAllJalanBerlubangInferences()
        }
    }

}