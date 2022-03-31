package com.daotrung.wallpapers_2021.room

import androidx.lifecycle.LiveData

class MyWallRepository(private val mywallDao : IDao) {

    val allMyWall: LiveData<List<MyWallPaper>> = mywallDao.getAllNotes()

    suspend fun insert(mywallpaper: MyWallPaper){
        mywallDao.insert(mywallpaper)
    }

    suspend fun delete(mywallpaper: MyWallPaper){
        mywallDao.delete(mywallpaper)
    }

    suspend fun update(mywallpaper: MyWallPaper){
        mywallDao.update(mywallpaper)
    }

}