package my.id.jeremia.potholetracker.data.repository

import android.media.Image
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import javax.inject.Inject

class TensorflowRepository @Inject constructor(
    private val interpreter: Interpreter,
    private val imageProcessor: ImageProcessor,
){

    companion object{
        val OUTPUT_SIZE = 1
    }
    fun startInference(inputData: TensorImage):FloatArray{
            val input = inputData.tensorBuffer.buffer
            val output = Array(1) { FloatArray(OUTPUT_SIZE) }
            interpreter.run(input, output)
            return output[0]
    }

    fun processImage(image: TensorImage):TensorImage = imageProcessor.process(image)

}