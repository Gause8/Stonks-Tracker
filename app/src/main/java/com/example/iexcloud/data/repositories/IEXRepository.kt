package com.example.iexcloud.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.iexcloud.data.network.Api
import com.example.iexcloud.data.network.response.IEXResponse
import com.example.iexcloud.data.room.StockEntity
import com.example.iexcloud.data.room.WatchlistDao
import com.example.iexcloud.util.Convert
import retrofit2.Response

class IEXRepository(private val watchlistDao: WatchlistDao) {

    suspend fun getStockInfo(name: String): Response<IEXResponse>{
        return Api().getStockInfo(name)
    }
    suspend fun addStock(stock: IEXResponse){
        watchlistDao.insertStockEntitiy(Convert.responseToEntity(stock))
    }

    fun readAllData(): LiveData<List<StockEntity>>{
        return watchlistDao.getAll()
    }
    suspend fun getSymbol(symbol: String): StockEntity{
        return watchlistDao.getSymbol(symbol)
    }
}