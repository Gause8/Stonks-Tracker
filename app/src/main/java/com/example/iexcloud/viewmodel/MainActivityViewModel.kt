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
import com.example.iexcloud.util.Coroutines
import kotlinx.coroutines.runBlocking
import java.util.ArrayList

private const val TAG = "MainActivityViewModel"

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    var mainListener: MainListener? = null
    private val repository: IEXRepository
    val readAllData: LiveData<List<StockEntity>>
    private val watchlistDao: WatchlistDao
    var chartResponse: IEXChartResponse = IEXChartResponse()

    private val _detailStock = MutableLiveData<StockEntity>()
    val detailStock: LiveData<StockEntity> = _detailStock

    private val _chartStock = MutableLiveData<IEXChartResponse>()
    val chartStock: LiveData<IEXChartResponse> = _chartStock



    fun getStock(symbol: String){
        Log.d(TAG, "Getting Info")
        Coroutines.io {
            val response =  repository.getStockInfo(symbol)
//            val sym = repository.getSymbol(response.body()?.symbol!!)
            if (response.isSuccessful){
                Log.d(TAG, response.body()?.symbol!!)
                repository.addStock(response.body()!!)
            }

        }

    }

    fun setDetailStock(stock: StockEntity){
        _detailStock.value = stock
        Log.d(TAG, "setting stock detail:  ${stock.symbol}")
    }

    fun getChart(symbol: String): IEXChartResponse{
         Coroutines.io {
            val response = repository.getChartInfo(symbol)
             chartResponse = response.body()!!
            if (response.isSuccessful){
                for (item in response.body()!!){
                   chartStock.value?.add(item)
                }
            }
        }
        return chartResponse
    }


    init {
        watchlistDao = WatchlistDB.getWatchListDatabase(application).getWatchListDao()
        repository = IEXRepository(watchlistDao)
        readAllData = repository.readAllData()

    }

}