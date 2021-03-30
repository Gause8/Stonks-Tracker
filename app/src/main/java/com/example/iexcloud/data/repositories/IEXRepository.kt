package com.example.iexcloud.data.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.iexcloud.data.network.Api
import com.example.iexcloud.data.network.response.IEXChartResponse
import com.example.iexcloud.data.network.response.IEXResponse
import com.example.iexcloud.data.room.StockEntity
import com.example.iexcloud.data.room.WatchlistDao
import com.example.iexcloud.util.Convert
import retrofit2.Response

class IEXRepository(private val watchlistDao: WatchlistDao) {

    /*
    * getting the stock info based on the symbol
    * */
    suspend fun getStockInfo(name: String): Response<IEXResponse>{
        return Api().getStockInfo(name)
    }
    /*
    * getting the chart info based on the symbol
    * */
    suspend fun getChartInfo(symbol: String): Response<IEXChartResponse>{
        return Api().getChartInfo(symbol)
    }
    /*
    * adding a stock to the DB
    * */
    suspend fun addStock(stock: StockEntity){
        watchlistDao.insertStockEntitiy(stock)
    }
    /*
    * reading all the data from DB
    * */
    fun readAllData(): LiveData<List<StockEntity>>{
        return watchlistDao.getAll()
    }
    /*
    * getting a stock by its symbol
    * */
    suspend fun getSymbol(symbol: String): StockEntity{
        return watchlistDao.getSymbol(symbol)
    }
    /*
    * deleting a stock from the DB
    * */
    suspend fun delete(stockEntity: StockEntity){
        watchlistDao.delete(stockEntity)
    }
    /*
    * getting stocks on a certian watchlist
    * */
     fun getWatchList(watchList: String): LiveData<List<StockEntity>>
            = watchlistDao.getWatchList(watchList)

    /*
    * getting the name of all the watchlists
    * */
     fun getNamedWatchlist():List<String> = watchlistDao.getNamedWatchList()
}