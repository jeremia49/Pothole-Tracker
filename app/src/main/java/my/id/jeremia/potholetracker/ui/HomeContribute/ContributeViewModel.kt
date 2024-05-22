package my.id.jeremia.potholetracker.ui.HomeContribute

import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.os.HandlerCompat.postDelayed
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.data.model.Location
import my.id.jeremia.potholetracker.utils.location.ClientLocationRequest
import javax.inject.Inject

@HiltViewModel
class ContributeViewModel @Inject constructor(
    val locationRequest: ClientLocationRequest
) : ViewModel() {


    private val _isCameraActive = MutableLiveData<Boolean>(false)
    val isCameraActive: LiveData<Boolean>
        get() = _isCameraActive

    private val _isGPSActive = MutableLiveData<Boolean>(false)
    val isGPSActive: LiveData<Boolean>
        get() = _isGPSActive

    private val _isInferenceStarted = MutableLiveData<Boolean>(false)
    val isInferenceStarted: LiveData<Boolean>
        get() = _isInferenceStarted

    private val _locationData = MutableLiveData<Location>()
    val locationData: LiveData<Location>
        get() = _locationData

    private val _currentImage = MutableLiveData<Bitmap>()
    val currentImage: LiveData<Bitmap>
        get() = _currentImage


    init {
        locationRequest.startLocationUpdate {
            if(it == null) return@startLocationUpdate
            setGPSActive()

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                _locationData.postValue(Location(
                    it.latitude,
                    it.longitude,
                    speed = it.speed,
                    accuracy = it.accuracy,
                    speedAccuracy = it.speedAccuracyMetersPerSecond,
                ))
            }else{
                _locationData.postValue(Location(
                    it.latitude,
                    it.longitude,
                    speed = it.speed,
                    accuracy = it.accuracy,
                ))
            }


        }
    }

    fun addInference(image: Bitmap, ){
//        viewModelScope.launch(Dispatchers.Main){
//            imageview.setImageBitmap(image)
//        }

    }


    fun setCameraActive(){
        viewModelScope.launch{
            _isCameraActive.postValue(true)
            delay(500)
            _isCameraActive.postValue(false)
        }
    }
    fun setGPSActive(){
        viewModelScope.launch{
            _isGPSActive.postValue(true)
            delay(500)
            _isGPSActive.postValue(false)
        }
    }

    fun updateCurrentImage(image: Bitmap){
        _currentImage.postValue(image)
    }

    fun toggleInference() {
        _isInferenceStarted.value = !_isInferenceStarted.value!!
    }


}