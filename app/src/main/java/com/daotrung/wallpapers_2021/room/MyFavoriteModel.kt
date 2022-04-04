package com.daotrung.wallpapers_2021.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFavoriteModel(application: Application):AndroidViewModel(application){
    val allPicFavorite : LiveData<List<MyFavoritePicture>>
    val repository_favorite : MyFavoriteRepository
    init {
        val dao = MyWallPaperDatabase.getDatabase(application).getMyWallDao()
        repository_favorite = MyFavoriteRepository(dao)
        allPicFavorite = repository_favorite.allMyFavorite

    }

    fun deleteFavorite(myfavoritepicture: MyFavoritePicture) = viewModelScope.launch(Dispatchers.IO ) {
        repository_favorite.delete(myfavoritepicture)
    }

    fun updateFavorite(myfavoritepicture: MyFavoritePicture) = viewModelScope.launch(Dispatchers.IO){
        repository_favorite.update(myfavoritepicture)
    }
    fun addFavorite(myfavoritepicture: MyFavoritePicture) = viewModelScope.launch(Dispatchers.IO){
        repository_favorite.insert(myfavoritepicture)
    }
}