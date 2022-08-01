package com.example.cameraandgallerypractice.DB

import com.example.cameraandgallerypractice.CLASS.ImageModel

interface DbHelper {

    fun insertImage(imageModel: ImageModel)

    fun getAllImages():ArrayList<ImageModel>

}