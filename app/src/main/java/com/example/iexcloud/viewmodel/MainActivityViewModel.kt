package com.example.iexcloud.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.iexcloud.data.repositories.IEXRepository
import com.example.iexcloud.data.room.StockEntity
import com.example.iexcloud.data.room.WatchlistDB
import com.example.iexcloud.data.room.WatchlistDao
import com.example.iexcloud.ui.MainListener
import com.example.iexcloud.util.Coroutines

private const val TAG = "MainActivityViewModel"

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    var mainListener: MainListener? = null
    private val repository: IEXRepository
    val readAllData: LiveData<List<StockEntity>>
    private val watchlistDao: WatchlistDao



    fun getStock(symbol: String){
        Log.d(TAG, "Getting Info")
        Coroutines.io {
            val response =  repository.getStockInfo(symbol)
            val sym = repository.getSymbol(response.body()?.symbol!!)
            if (response.isSuccessful){
                Log.d(TAG, response.body()?.symbol!!)
                repository.addStock(response.body()!!)
            }

        }

    }


    init {
        watchlistDao = WatchlistDB.getWatchListDatabase(application).getWatchListDao()
        repository = IEXRepository(watchlistDao)
        readAllData = repository.readAllData()

    }

}