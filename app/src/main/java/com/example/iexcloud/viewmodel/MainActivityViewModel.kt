package com.example.iexcloud.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.iexcloud.data.network.response.IEXChartResponse
import com.example.iexcloud.data.repositories.IEXRepository
import com.example.iexcloud.data.room.StockEntity
import com.example.iexcloud.data.room.WatchlistDB
import com.example.iexcloud.data.room.WatchlistDao
import com.example.iexcloud.ui.MainListener
import com.example.iexcloud.util.Convert
import com.example.iexcloud.util.Coroutines
import kotlinx.coroutines.*
import java.util.ArrayList

private const val TAG = "MainActivityViewModel"

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    var mainListener: MainListener? = null
    private val repository: IEXRepository
    private val readAllData: LiveData<List<StockEntity>>
    //val namedList: List<String>
    var watchListData: LiveData<List<StockEntity>>
    private val watchlistDao: WatchlistDao
    var chartResponse: IEXChartResponse = IEXChartResponse()

    private val _detailStock = MutableLiveData<StockEntity>()
    val detailStock: LiveData<StockEntity> = _detailStock

    private val _chartStock = MutableLiveData<IEXChartResponse>()
    val chartStock: LiveData<IEXChartResponse> = _chartStock



    fun getStock(symbol: String,watchListName: String){
        Log.d(TAG, "Getting Info")
        Coroutines.io {
            val response =  repository.getStockInfo(symbol)
            if (response.isSuccessful){
                Log.d(TAG, response.body()?.symbol!!)
                val temp = Convert.responseToEntity(response.body()!!, watchListName)
                repository.addStock(temp)
            }

        }

    }

    fun setDetailStock(stock: StockEntity){
        _detailStock.value = stock

    }

    suspend fun getChart(symbol: String): IEXChartResponse{
            val response = repository.getChartInfo(symbol)
             chartResponse = response.body()!!
            if (response.isSuccessful){
                for (item in response.body()!!){
                   chartStock.value?.add(item)
                }
            }
        return chartResponse
    }

    fun deleteStock(stock: StockEntity){
        Coroutines.io {
            repository.delete(stock)
        }
    }
    fun getWatchlist(watchListName: String){

        watchListData = repository.getWatchList(watchListName)

    }
    fun getWatchlistNames(): List<String>{
        return repository.getNamedWatchlist()
    }


    init {
        watchlistDao = WatchlistDB.getWatchListDatabase(application).getWatchListDao()
        repository = IEXRepository(watchlistDao)
        readAllData = repository.readAllData()
        watchListData = repository.getWatchList("My first list")
    }

}