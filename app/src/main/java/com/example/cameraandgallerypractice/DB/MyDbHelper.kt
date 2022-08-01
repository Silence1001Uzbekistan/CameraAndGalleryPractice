package com.example.cameraandgallerypractice.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.cameraandgallerypractice.CLASS.ImageModel
import com.example.cameraandgallerypractice.CONSTANT.Constant

class MyDbHelper(context: Context) :
    SQLiteOpenHelper(context, Constant.DB_NAME, null, Constant.DB_VERSION), DbHelper {
    override fun onCreate(p0: SQLiteDatabase?) {

        val query =
            "create table image_table(id integer primary key autoincrement not null,img_path text not null,image blob not null)"
        p0?.execSQL(query)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {


    }

    override fun insertImage(imageModel: ImageModel) {

        val database = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put("img_path", imageModel.imagePath)
        contentValues.put("image", imageModel.image)
        database.insert("image_table", null, contentValues)
        database.close()

    }

    override fun getAllImages(): ArrayList<ImageModel> {

        val list = ArrayList<ImageModel>()

        val query = "select * from image_table"

        val database = this.readableDatabase

        val cursor = database.rawQuery(query, null)

        if (cursor.moveToFirst()){

            do {

                val imageModel = ImageModel()
                imageModel.id = cursor.getInt(0)
                imageModel.imagePath = cursor.getString(1)
                imageModel.image = cursor.getBlob(2)
                list.add(imageModel)

            }while (cursor.moveToNext())

        }

        return list

    }
}