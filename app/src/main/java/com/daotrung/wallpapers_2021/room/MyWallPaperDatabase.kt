package com.daotrung.wallpapers_2021.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MyWallPaper::class], version = 1, exportSchema = false)
abstract class MyWallPaperDatabase: RoomDatabase() {
    abstract fun getMyWallDao() : IDao

    companion object{

        @Volatile
        private var INSTANCE: MyWallPaperDatabase? = null

        fun getDatabase(context: Context):MyWallPaperDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyWallPaperDatabase::class.java,
                    "mywall_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}