package com.daotrung.wallpapers_2021.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "myPicTable")
data class MyPicturePaper  (
    @ColumnInfo(name = "urlP")
    var  myPicUrl: String

){
    @PrimaryKey(autoGenerate = true)
    var idP = 0
}