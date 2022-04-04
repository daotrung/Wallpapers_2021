package com.daotrung.wallpapers_2021.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "myPicFavorite")
data class MyFavoritePicture(
    @ColumnInfo(name = "urlHeart")
    var myUrlHeart : String
){
    @PrimaryKey(autoGenerate = true)
    var idH = 0
}
