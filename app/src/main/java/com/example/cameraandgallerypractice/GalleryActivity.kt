package com.example.cameraandgallerypractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cameraandgallerypractice.CLASS.ImageModel
import com.example.cameraandgallerypractice.DB.MyDbHelper
import com.example.cameraandgallerypractice.databinding.ActivityGalleryBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding
    var REQUEST_CODE = 100

    lateinit var myDbHelper: MyDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        myDbHelper = MyDbHelper(this)

        binding.galOldBtn.setOnClickListener {

            pickImageFromOldGallery()


        }

        binding.galNewBtn.setOnClickListener {

            pickImageFromNewGaallery()

        }

        binding.deleteBtn.setOnClickListener {

            clearImage()

        }

    }

    private fun pickImageFromOldGallery() {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        val uri = data?.data ?: return

        binding.galleryImage.setImageURI(uri)

        val openInputStream = contentResolver?.openInputStream(uri)
        val file = File(filesDir, "image.jpg")
        val fileOutputStream = FileOutputStream(file)
        openInputStream?.copyTo(fileOutputStream)
        fileOutputStream?.close()

    }


    private fun pickImageFromNewGaallery() {

        getImageContent.launch("image/*")

    }

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            uri ?: return@registerForActivityResult
            binding.galleryImage.setImageURI(uri)

            val openInputStream = contentResolver?.openInputStream(uri)
            val file = File(filesDir, "image.jpg")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()
            val fileAbsolutePath = file.absolutePath

            val fileInputStream = FileInputStream(file)
            val readBytes = fileInputStream.readBytes()

            val imageModel = ImageModel(fileAbsolutePath, readBytes)
            myDbHelper.insertImage(imageModel)

        }

    private fun clearImage() {

        val filesDir = filesDir

        if (filesDir.isDirectory) {

            val listFiles = filesDir.listFiles()

            for (listFile in listFiles) {
                listFile.delete()
            }

        }

    }


}