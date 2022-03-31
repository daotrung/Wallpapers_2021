package com.daotrung.wallpapers_2021.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyWallpaperViewModel (application: Application) : AndroidViewModel(application){
    val allWallPaper : LiveData<List<MyWallPaper>>
    val repository : MyWallRepository
    init {
        val dao = MyWallPaperDatabase.getDatabase(application).getMyWallDao()
        repository = MyWallRepository(dao)
        allWallPaper = repository.allMyWall
    }
    fun deleteMyWallPaper(mywallpaper: MyWallPaper) = viewModelScope.launch(Dispatchers.IO ) {
        repository.delete(mywallpaper)
    }

    fun updateMyWallPaper(mywallpaper: MyWallPaper) = viewModelScope.launch(Dispatchers.IO){
        repository.update(mywallpaper)
    }
    fun addMyWallPaper(mywallpaper: MyWallPaper) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(mywallpaper)
    }
}