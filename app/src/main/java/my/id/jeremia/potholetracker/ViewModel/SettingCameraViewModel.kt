package my.id.jeremia.potholetracker.ViewModel

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Rect
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

class SettingCameraViewModel : ViewModel(){


    val _previewView = mutableStateOf<PreviewView?>(null)
    val previewView : State<PreviewView?> = _previewView

    val _bitmapImage  = mutableStateOf<Bitmap?>(null)
    val bitmapImage : State<Bitmap?> = _bitmapImage

    val _orientation  = mutableIntStateOf(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val orientation : State<Int> = _orientation

    val _cropRect = mutableStateOf<Rect?>(null)
    val cropRect : State<Rect?> = _cropRect


    fun setPreviewView(p:PreviewView){
        _previewView.value = p
    }

    fun setCropRect(r:Rect){
        _cropRect.value = r
    }

    fun setOrientation(orientation:Int){
        _orientation.intValue = orientation
    }

    fun setBitmapImage(bp:Bitmap){
        _bitmapImage.value = bp
    }

    fun resetBitmapImage(){
        _bitmapImage.value = null
    }

}