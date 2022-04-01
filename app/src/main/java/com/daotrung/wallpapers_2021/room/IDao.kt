package com.daotrung.wallpapers_2021.room

import androidx.lifecycle.LiveData
import androidx.room.*
import java.nio.file.Path

@Dao
interface IDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(mypicturepaper: MyPicturePaper)

    @Update
    suspend fun update(mypicturepaper: MyPicturePaper)

    @Delete
    suspend fun delete(mypicturepaper: MyPicturePaper)

    @Query("Select * from myPicTable order by idP asc")
    fun getAllPic(): LiveData<List<MyPicturePaper>>

    @Query("Select Exists (Select * from myPicTable where urlP = :path )")
    fun isExistPic(path: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(mywallpaper: MyWallPaper)

    @Update
    suspend fun update(mywallpaper: MyWallPaper)

    @Delete
    suspend fun delete(mywallpaper: MyWallPaper)

    @Query("Select DISTINCT * from myWallTable order by id asc")
    fun getAllNotes(): LiveData<List<MyWallPaper>>

    @Query("Select Exists (Select * from myWallTable where url = :pathWall )")
    fun isExistWall(pathWall: String): Boolean

}