package com.example.iexcloud.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(entities = [StockEntity::class], version = 1)
abstract class WatchlistDB: RoomDatabase() {
    /*
    * Setting up Room DB
    * */

    abstract fun getWatchListDao(): WatchlistDao

    companion object{
        @Volatile
        private var INSTANCE: WatchlistDB? = null

        fun getWatchListDatabase(context: Context): WatchlistDB{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                        context,
                        WatchlistDB::class.java,
                        "watchlist_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}