package my.id.jeremia.potholetracker.utils.camera

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.lang.Thread.sleep
import java.nio.ByteBuffer


typealias AnalyzerListener = (bp: Bitmap) -> Unit

class Analyzer(private val listener:AnalyzerListener) : ImageAnalysis.Analyzer{
    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    override fun analyze(image: ImageProxy) {
        val rotationDegrees = image.imageInfo.rotationDegrees
        val rotatedBitmap = rotateBitmap(image.toBitmap(), rotationDegrees)
        listener(rotatedBitmap)
        image.close()
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}