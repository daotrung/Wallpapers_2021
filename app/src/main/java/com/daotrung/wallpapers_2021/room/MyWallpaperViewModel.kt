package com.daotrung.wallpapers_2021.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyWallpaperViewModel (application: Application) : AndroidViewModel(application){
    val allWallPaper : LiveData<List<MyWallPaper>>
    val repository_mywall : MyWallRepository

    init {
        val dao = MyWallPaperDatabase.getDatabase(application).getMyWallDao()
        repository_mywall = MyWallRepository(dao)
        allWallPaper = repository_mywall.allMyWall
    }
    fun deleteMyWallPaper(mywallpaper: MyWallPaper) = viewModelScope.launch(Dispatchers.IO ) {
        repository_mywall.delete(mywallpaper)
    }

    fun updateMyWallPaper(mywallpaper: MyWallPaper) = viewModelScope.launch(Dispatchers.IO){
        repository_mywall.update(mywallpaper)
    }
    fun addMyWallPaper(mywallpaper: MyWallPaper) = viewModelScope.launch(Dispatchers.IO){
        repository_mywall.insert(mywallpaper)
    }
}