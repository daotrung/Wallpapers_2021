package com.daotrung.wallpapers_2021.room

import androidx.lifecycle.LiveData

class MyFavoriteRepository (private val myfavorite:IDao){
    val allMyFavorite : LiveData<List<MyFavoritePicture>> = myfavorite.getAllFavorite()

    suspend fun insert(myfavoritepicture: MyFavoritePicture){
        myfavorite.insert(myfavoritepicture)
    }
    suspend fun delete(myfavoritepicture: MyFavoritePicture){
        myfavorite.delete(myfavoritepicture)
    }

    suspend fun update(myfavoritepicture: MyFavoritePicture){
        myfavorite.update(myfavoritepicture)
    }


}