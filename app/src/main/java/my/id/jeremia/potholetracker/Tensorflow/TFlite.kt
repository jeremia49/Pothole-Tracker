package my.id.jeremia.potholetracker.Tensorflow

import android.content.Context
import android.util.Log
import com.google.android.gms.tflite.client.TfLiteInitializationOptions
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.gms.vision.TfLiteVision
import org.tensorflow.lite.task.gms.vision.classifier.ImageClassifier

object TFlite {

    var imageClassifier : ImageClassifier? = null;
    private val modelName = "potholev6_metadata.tflite"

    fun initializeTFLiteVision(ctx:Context){
        if(imageClassifier!=null) return;
        val options = TfLiteInitializationOptions.builder()
            .setEnableGpuDelegateSupport(true)
            .build()

        TfLiteVision.initialize(ctx, options).addOnSuccessListener {
            Log.i("TFLITEVISION", "Berhasil initialize TFLITEVISION dengan GPU")
            initializeModel(ctx)
        }.addOnFailureListener {
            TfLiteVision.initialize(ctx).addOnSuccessListener {
                Log.i("TFLITEVISION", "Berhasil initialize TFLITEVISION tanpa GPU")
                initializeModel(ctx)
            }.addOnFailureListener{
                Log.e("TFLITEVISION", "Gagal initialize TFLITE-VISION ${it.message}")
            }
        }
        return
    }

    fun initializeModel(ctx:Context){
        val optionsBuilder =
            ImageClassifier.ImageClassifierOptions.builder()
                .setScoreThreshold(0f)
                .setMaxResults(2)

        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(1)
//            .useGpu()

        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try{
            imageClassifier =
                ImageClassifier.createFromFileAndOptions(
                    ctx, modelName, optionsBuilder.build()
                )
            Log.i("IMAGECLASSIFIER", "Berhasil initialize imageclassifier")
        }catch(e:Exception){
            Log.e("IMAGECLASSIFIER", "Gagal initialize imageclassifier ${e.message}")
        }



    }

}