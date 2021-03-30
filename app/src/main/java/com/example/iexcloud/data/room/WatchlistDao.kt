package com.example.iexcloud.data.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WatchlistDao {

    /*
    * inserting a stock into the DB
    * */
    @Insert(entity = StockEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStockEntitiy(data: StockEntity)

    /*
    * deleting a stock in the DB
    * */
    @Delete
    fun delete(data: StockEntity)

    /*
    * getting all the stocks for the DB
    * */
    @Query("SELECT * FROM Watchlist")
    fun getAll(): LiveData<List<StockEntity>>

    /*
    * getting a certain stock by its symbol
    * */
    @Query("SELECT * FROM Watchlist WHERE symbol= :sym")
    suspend fun getSymbol(sym: String): StockEntity

    /*
    * getting certain watchlist by its name
    * */
    @Query("SELECT * FROM Watchlist WHERE watchlistName = :watchlist")
    fun getWatchList(watchlist: String): LiveData<List<StockEntity>>

    /*
    *getting the column for the watchlist name
    * */
    @Query("SELECT WatchlistName FROM Watchlist")
    fun getNamedWatchList(): List<String>

}