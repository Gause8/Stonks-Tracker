package com.example.iexcloud.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.iexcloud.data.network.response.IEXResponse
import com.example.iexcloud.data.repositories.IEXRepository
import com.example.iexcloud.ui.MainListener
import com.example.iexcloud.util.Coroutines

private const val TAG = "MainActivityViewModel"

class MainActivityViewModel : ViewModel() {
    var mainListener: MainListener? = null
    private var listOfResponses: ArrayList<IEXResponse>? = ArrayList<IEXResponse>()

    fun getStock(symbol: String){
        Log.d(TAG, "Getting Info")
        Coroutines.main {
            val response = IEXRepository().getStockInfo(symbol)

            if (response.isSuccessful) {
                Log.d(TAG, response.body()?.symbol!!)
                listOfResponses?.add(response.body()!!)
                Log.d(TAG, listOfResponses?.size.toString())
                mainListener?.OnSuccess(listOfResponses!!)
            }

        }
    }

}