package com.daotrung.wallpapers_2021.room

import androidx.lifecycle.LiveData
import androidx.room.*
@Dao
interface IDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(mywallpaper: MyWallPaper)

    @Update
    suspend fun update(mywallpaper: MyWallPaper)

    @Delete
    suspend fun delete(mywallpaper: MyWallPaper)

    @Query("Select DISTINCT * from myWallTable order by url")
    fun getAllNotes(): LiveData<List<MyWallPaper>>

}