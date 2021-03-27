package com.example.iexcloud.data.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WatchlistDao {

    @Insert(entity = StockEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStockEntitiy(data: StockEntity)

    @Delete
    fun delete(data: StockEntity)

    @Query("SELECT * FROM Watchlist")
    fun getAll(): LiveData<List<StockEntity>>

    @Query("SELECT * FROM Watchlist WHERE symbol= :sym")
    suspend fun getSymbol(sym: String): StockEntity

}