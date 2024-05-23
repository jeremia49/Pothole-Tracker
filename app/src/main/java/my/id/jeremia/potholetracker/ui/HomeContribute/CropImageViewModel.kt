package my.id.jeremia.potholetracker.ui.HomeContribute

import android.graphics.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import my.id.jeremia.potholetracker.data.repository.CropRepository
import javax.inject.Inject


@HiltViewModel
class CropImageViewModel @Inject constructor(
    private val cropRect: CropRepository
) : ViewModel() {

    fun setCropRect(r: Rect){
        viewModelScope.launch {
            cropRect.setCropRect(r)
        }
    }

    fun getCropRect(): Rect {
        return cropRect.cropRect.value!!
    }

}
