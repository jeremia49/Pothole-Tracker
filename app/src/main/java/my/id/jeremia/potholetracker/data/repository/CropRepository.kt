package my.id.jeremia.potholetracker.data.repository

import android.graphics.Rect
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import my.id.jeremia.potholetracker.data.local.datastore.CropDataStore
import my.id.jeremia.potholetracker.data.model.Auth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CropRepository @Inject constructor(
    val cropDataStore: CropDataStore
) {

    private val _cropRect = MutableLiveData<Rect>(Rect())
    val cropRect: LiveData<Rect> = _cropRect

    init {
        var left = 0
        var top = 0
        var right = 0
        var bottom = 0

        CoroutineScope(Dispatchers.Default).launch {
            cropDataStore.getLeft().collect {
                left = (it ?: "0").toInt()
                _cropRect.postValue(Rect(left, top, right, bottom))
            }
        }
        CoroutineScope(Dispatchers.Default).launch {
            cropDataStore.getTop().collect {
                top = (it ?: "0").toInt()
                _cropRect.postValue(Rect(left, top, right, bottom))
            }
        }
        CoroutineScope(Dispatchers.Default).launch {
            cropDataStore.getRight().collect {
                right = (it ?: "0").toInt()
                _cropRect.postValue(Rect(left, top, right, bottom))
            }
        }
        CoroutineScope(Dispatchers.Default).launch {
            cropDataStore.getBottom().collect {
                bottom = (it ?: "0").toInt()
                _cropRect.postValue(Rect(left, top, right, bottom))
            }
        }
    }

    suspend fun setCropRect(rect: Rect) {
        cropDataStore.setLeft(rect.left.toString())
        cropDataStore.setTop(rect.top.toString())
        cropDataStore.setRight(rect.right.toString())
        cropDataStore.setBottom(rect.bottom.toString())
    }

}