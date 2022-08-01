package com.example.cameraandgallerypractice

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.cameraandgallerypractice.databinding.ActivityCameraBinding
import kotlinx.coroutines.currentCoroutineContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    lateinit var currentImagePath: String

    var REQUEST_CODE = 1

    lateinit var photoURI: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        //1

        binding.camOldBtn.setOnClickListener {

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)

            val photoFile = createImageFile()

            photoFile?.also {

                val photoUri =
                    FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, it)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_CODE)

            }

        }

        //2
        val imageFile = createImageFile()
        photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, imageFile)

        binding.camNewBtn.setOnClickListener {

            getTakeImageContent.launch(photoURI)

        }

        binding.deleteBtn.setOnClickListener {



        }

    }


    //2
    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {


            if (it) {

                binding.cameraImage.setImageURI(photoURI)

                val openInputStream = contentResolver?.openInputStream(photoURI)
                val file = File(filesDir, "image.jpg")
                val fileOutputStream = FileOutputStream(file)
                openInputStream?.copyTo(fileOutputStream)
                openInputStream?.close()
                fileOutputStream.close()

            }


        }

    //1
    @Throws(IOException::class)
    private fun createImageFile(): File {

        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        val externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("JPEG_$format", ".jpg", externalFilesDir).apply {

            currentImagePath = absolutePath

        }


    }


    //1
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (::currentImagePath.isInitialized) {

            binding.cameraImage.setImageURI(Uri.fromFile(File(currentImagePath)))

        }

    }

}