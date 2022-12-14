package com.example.cameraandgallerypractice

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cameraandgallerypractice.DB.MyDbHelper
import com.example.cameraandgallerypractice.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var myDbHelper: MyDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        myDbHelper = MyDbHelper(this)

        var list = myDbHelper.getAllImages()

//        binding.pathImage.setImageURI(Uri.parse(list[0].imagePath))


        /*val bitmap = BitmapFactory.decodeByteArray(list[0].image, 0, list[0].image!!.size)
        binding.byteImage.setImageBitmap(bitmap)
*/

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                    showActions()

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {


                }
            }).check()


    }

    private fun showActions(){

        binding.galleryBtn.setOnClickListener {

            startActivity(Intent(this, GalleryActivity::class.java))

        }

        binding.cameraBtn.setOnClickListener {

            startActivity(Intent(this, CameraActivity::class.java))

        }
    }

}