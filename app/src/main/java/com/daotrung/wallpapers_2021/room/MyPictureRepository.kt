package com.daotrung.wallpapers_2021.room

import androidx.lifecycle.LiveData

class MyPictureRepository (private val mypic:IDao){
    val allmyPic : LiveData<List<MyPicturePaper>> = mypic.getAllPic()


    suspend fun insert(mypicturepaper: MyPicturePaper){
        mypic.insert(mypicturepaper)
    }

    suspend fun delete(mypicturepaper: MyPicturePaper){
        mypic.delete(mypicturepaper)
    }

    suspend fun update(mypicturepaper: MyPicturePaper){
        mypic.update(mypicturepaper)
    }
}