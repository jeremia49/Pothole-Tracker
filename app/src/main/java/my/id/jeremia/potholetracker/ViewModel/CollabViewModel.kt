package my.id.jeremia.potholetracker.ViewModel

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Rect
import androidx.camera.view.PreviewView
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.Data.InferenceData
import my.id.jeremia.potholetracker.Data.InferenceRepository
import my.id.jeremia.potholetracker.Data.LocationData
import my.id.jeremia.potholetracker.Graph
import java.util.Timer

class CollabViewModel(
    private val inferenceRepository:InferenceRepository = Graph.inferenceRepository
) : ViewModel(){
    private val _isInferenceStarted = mutableStateOf<Boolean>(false)
    val isInferenceStarted : State<Boolean> = _isInferenceStarted

    private val _originalbitmapImage  = mutableStateOf<Bitmap?>(null)
    val originalBitmapImage : State<Bitmap?> = _originalbitmapImage

    private val _bitmapImage  = mutableStateOf<Bitmap?>(null)
    val bitmapImage : State<Bitmap?> = _bitmapImage

    private val _orientation  = mutableIntStateOf(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val orientation : State<Int> = _orientation

    private val _cropRect = mutableStateOf<Rect?>(null)
    val cropRect : State<Rect?> = _cropRect

    private val _timer = mutableStateOf<Timer?>(null)
    val timer : State<Timer?> = _timer

    val _locationData = mutableStateOf<LocationData?>(null)
    val locationData : State <LocationData?> = _locationData

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


    fun setCropRect(r: Rect){
        _cropRect.value = r
    }

    fun setOrientation(orientation:Int){
        _orientation.intValue = orientation
    }

    fun setBitmapImage(bp: Bitmap){
        _bitmapImage.value = bp
    }

    fun setOriginalBitmapImage(bp: Bitmap){
        _originalbitmapImage.value = bp
    }

    fun setTimer(t : Timer){
        _timer.value = t
    }

    fun cancelTimer(){
        _timer.value!!.cancel()
        _timer.value = null
    }

    fun updateLocationData(locationData: LocationData){
        _locationData.value = locationData
    }

    fun startInference(){
        _isInferenceStarted.value = true
    }
    fun stopInference(){
        _isInferenceStarted.value = false
    }

}