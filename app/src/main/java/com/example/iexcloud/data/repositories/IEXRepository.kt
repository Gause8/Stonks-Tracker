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

    suspend fun getStockInfo(name: String): Response<IEXResponse>{
        return Api().getStockInfo(name)
    }
    suspend fun getChartInfo(symbol: String): Response<IEXChartResponse>{
        return Api().getChartInfo(symbol)
    }
    suspend fun addStock(stock: StockEntity){
        watchlistDao.insertStockEntitiy(stock)
    }

    fun readAllData(): LiveData<List<StockEntity>>{
        Log.d("REPOSITORY", "reading data")
        return watchlistDao.getAll()
    }
    suspend fun getSymbol(symbol: String): StockEntity{
        return watchlistDao.getSymbol(symbol)
    }
    suspend fun delete(stockEntity: StockEntity){
        watchlistDao.delete(stockEntity)
    }

     fun getWatchList(watchList: String): LiveData<List<StockEntity>>
            = watchlistDao.getWatchList(watchList)

     fun getNamedWatchlist():List<String> = watchlistDao.getNamedWatchList()
}