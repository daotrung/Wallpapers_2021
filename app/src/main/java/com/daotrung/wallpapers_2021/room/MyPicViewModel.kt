package com.daotrung.wallpapers_2021.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyPicViewModel(application: Application):AndroidViewModel(application) {
    val allPicPaper : LiveData<List<MyPicturePaper>>
    val repository_mypic : MyPictureRepository

    init {
        val dao = MyWallPaperDatabase.getDatabase(application).getMyWallDao()
        repository_mypic = MyPictureRepository(dao)
        allPicPaper = repository_mypic.allmyPic
    }
    fun deletePicPaper(mypicpaper: MyPicturePaper) = viewModelScope.launch(Dispatchers.IO ) {
        repository_mypic.delete(mypicpaper)
    }

    fun updatePicPaper(mypicpaper: MyPicturePaper) = viewModelScope.launch(Dispatchers.IO){
        repository_mypic.update(mypicpaper)
    }
    fun addPicPaper(mypicpaper: MyPicturePaper) = viewModelScope.launch(Dispatchers.IO){
        repository_mypic.insert(mypicpaper)
    }
}