package my.id.jeremia.potholetracker.utils.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File


fun readBitmap(path:String):Bitmap{
    val bmOptions = BitmapFactory.Options()
    val bitmap = BitmapFactory.decodeFile(path, bmOptions)
    return bitmap
}