package my.id.jeremia.potholetracker.ViewModel

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Rect
import androidx.camera.view.PreviewView
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.Timer

class CollabViewModel : ViewModel(){

    val _previewView = mutableStateOf<PreviewView?>(null)
    val previewView : State<PreviewView?> = _previewView

    val _originalbitmapImage  = mutableStateOf<Bitmap?>(null)
    val originalBitmapImage : State<Bitmap?> = _originalbitmapImage

    val _bitmapImage  = mutableStateOf<Bitmap?>(null)
    val bitmapImage : State<Bitmap?> = _bitmapImage

    val _orientation  = mutableIntStateOf(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val orientation : State<Int> = _orientation

    val _cropRect = mutableStateOf<Rect?>(null)
    val cropRect : State<Rect?> = _cropRect

    val _timer = mutableStateOf<Timer?>(null)
    val timer : State<Timer?> = _timer

    fun setPreviewView(p: PreviewView){
        _previewView.value = p
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

}