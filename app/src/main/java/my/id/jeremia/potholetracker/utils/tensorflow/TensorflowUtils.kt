package my.id.jeremia.potholetracker.utils.tensorflow

import android.content.Context
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

object TensorflowUtils {

    @Throws(IOException::class)
    fun loadModelFile(ctx: Context, modelPath: String): MappedByteBuffer {
        val fileDescriptor = ctx.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

}