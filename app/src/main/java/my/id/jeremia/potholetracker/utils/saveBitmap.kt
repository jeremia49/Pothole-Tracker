package my.id.jeremia.potholetracker.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun saveBitmapToFile(context: Context, bitmap: Bitmap): File? {
    // Get the directory for temporary files
    val tempDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    // Create a temporary file
    var tempFile: File? = null
    try {
        tempFile = File.createTempFile("temp_image", ".jpg", tempDir)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    if (tempFile != null) {
        try {
            // Write the bitmap data to the file
            val fos = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return tempFile
}