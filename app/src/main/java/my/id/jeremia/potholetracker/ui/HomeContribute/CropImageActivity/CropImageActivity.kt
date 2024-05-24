package my.id.jeremia.potholetracker.ui.HomeContribute.CropImageActivity

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.canhub.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import my.id.jeremia.potholetracker.databinding.ActivityCropImageBinding
import my.id.jeremia.potholetracker.utils.image.readBitmap


@AndroidEntryPoint
class CropImageActivity : AppCompatActivity() {
    private lateinit var cropImageView: CropImageView

    private lateinit var viewBinding: ActivityCropImageBinding

    val viewModel: CropImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewBinding = ActivityCropImageBinding.inflate(layoutInflater)
        setContentView(viewBinding.main)

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cropImageView = viewBinding.cropImageView

        val imgUrl = intent.getStringExtra("imgUrl")
        val bitmap = readBitmap(imgUrl!!)


        cropImageView.setImageBitmap(bitmap)

        viewBinding.savebtn.setOnClickListener {
            val crop = cropImageView.cropRect
            if(crop != null){
                viewModel.setCropRect(crop)
            }else{
                viewModel.setCropRect(Rect(0,0,bitmap.width,bitmap.height))
            }
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        viewBinding.resetbtn.setOnClickListener {
            cropImageView.cropRect = Rect(0,0,bitmap.width,bitmap.height)
        }

        val savedRect = viewModel.getCropRect()
        cropImageView.cropRect = savedRect

//        cropImageView.setOnCropWindowChangedListener {
//            println("crop window changed" + cropImageView.cropRect)
////            val it1 = cropImageView.cropRect
////            if (it1 != null) {
////                viewModel.setCropRect(it1)
////            }
//        }
    }
}
