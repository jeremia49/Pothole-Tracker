package my.id.jeremia.potholetracker.ui.HomeContribute.ContributeActivity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.id.jeremia.potholetracker.data.local.db.entity.InferenceData
import my.id.jeremia.potholetracker.data.model.Location
import my.id.jeremia.potholetracker.data.repository.CropRepository
import my.id.jeremia.potholetracker.data.repository.LocalInferenceRepository
import my.id.jeremia.potholetracker.utils.image.saveBitmapToFile
import my.id.jeremia.potholetracker.utils.location.ClientLocationRequest
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ContributeViewModel @Inject constructor(
    @ApplicationContext val ctx : Context,
    val locationRequest: ClientLocationRequest,
    val cropRepository: CropRepository,
    val localInferenceRepository: LocalInferenceRepository,
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

    private val _croppedImage = MutableLiveData<Bitmap>()
    val croppedImage : LiveData<Bitmap>
        get()= _croppedImage

    private var cropRect = Rect()

    init {
        viewModelScope.launch{
            cropRepository.cropRect.asFlow().collect{
                cropRect = it
            }
        }

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

    fun addInference(latitude:Float, longitude:Float, filePath:String, status:String ){
        viewModelScope.launch {
            val inferenceData = InferenceData(
                latitude = latitude,
                longitude = longitude,
                localImgPath = filePath,
                status = status,
                createdTimestamp = System.currentTimeMillis(),
                createdAt = Date()
            )
            localInferenceRepository.saveInference(inferenceData)
        }
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
        updateCroppedImage(image)
    }

    fun updateCroppedImage(image: Bitmap){
        var width = image.width
        var height = image.height

        if(cropRect.right != 0){
            width = cropRect.right - cropRect.left
        }
        if(cropRect.bottom != 0){
            height = cropRect.bottom - cropRect.top
        }

        var croppedImage : Bitmap;

        try{
            croppedImage =  Bitmap.createBitmap(
                image,
                cropRect.left,
                cropRect.top,
                width,
                height,
            )
        }catch (e: Exception){
            Log.e("Crop", e.message.toString())
            croppedImage =  image
        }
        _croppedImage.postValue(croppedImage)
    }

    fun toggleInference() {
        _isInferenceStarted.value = !_isInferenceStarted.value!!
    }

    fun saveImage(bitmap:Bitmap):String{
        val file = saveBitmapToFile(ctx, bitmap)
        if (file != null) {
            return file.absolutePath
        }
        return ""
    }

//    fun toggleInference() {
//        _isInferenceStarted.value = !_isInferenceStarted.value!!
//    }


}