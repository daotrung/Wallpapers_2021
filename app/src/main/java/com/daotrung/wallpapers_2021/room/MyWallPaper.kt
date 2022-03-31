package com.daotrung.wallpapers_2021.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "myWallTable")
class MyWallPaper (
    @ColumnInfo(name = "url")
     var  myUrl: String

){
   @PrimaryKey(autoGenerate = true)
   var id = 0
}