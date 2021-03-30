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


private const val TAG = "MainActivityViewModel"

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: IEXRepository
    private val readAllData: LiveData<List<StockEntity>>
    private val watchlistDao: WatchlistDao
    private val _detailStock = MutableLiveData<StockEntity>()
    val detailStock: LiveData<StockEntity> = _detailStock
    var watchListData: LiveData<List<StockEntity>>
    var mainListener: MainListener? = null
    var chartResponse: IEXChartResponse = IEXChartResponse()


    /*
    * inserting stock into database
    * symbol = the stock symbol
    * watchlistName = the name of the watchlist that it is added under*/
    //TODO cant have the same stock in a different watchlist
    fun addStock(symbol: String, watchListName: String){
        Coroutines.io {
            //network call to get the stock
            val response =  repository.getStockInfo(symbol)
            if (response.isSuccessful){
                //converting it to entity to add it to database
                val temp = Convert.responseToEntity(response.body()!!, watchListName)
                repository.addStock(temp)
            }

        }

    }

    /*
    * getting the stock data for the detail fragment
    * */
    fun setDetailStock(stock: StockEntity){
        _detailStock.value = stock

    }
    /*
    * getting the chart information for the detailed fragment
    */
    suspend fun getChart(symbol: String): IEXChartResponse{
            val response = repository.getChartInfo(symbol)
             chartResponse = response.body()!!
        return chartResponse
    }

    /*
    * deleting a specific stock
    * */
    fun deleteStock(stock: StockEntity){
        Coroutines.io {
            repository.delete(stock)
        }
    }
    /*
    * getting a specific watchlist from the db to display
    * */
    fun getWatchlist(watchListName: String){

        watchListData = repository.getWatchList(watchListName)

    }
    /*
    * getting all the name off the watch list to display on the menu
    * */
    fun getWatchlistNames(): List<String>{
        return repository.getNamedWatchlist()
    }

    /*
    * initializing all the basic stuff that is needed for the view model
    * */
    init {
        watchlistDao = WatchlistDB.getWatchListDatabase(application).getWatchListDao()
        repository = IEXRepository(watchlistDao)
        readAllData = repository.readAllData()
        watchListData = repository.getWatchList("My first list")
    }

}