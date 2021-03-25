package com.example.iexcloud.data.repositories

import com.example.iexcloud.data.network.Api
import com.example.iexcloud.data.network.response.IEXResponse
import retrofit2.Response

class IEXRepository {

    suspend fun getStockInfo(name: String): Response<IEXResponse>{
        return Api().getStockInfo(name)
    }
}