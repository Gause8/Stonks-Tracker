package com.example.iexcloud.data.network

import com.example.iexcloud.data.network.response.IEXChartResponse
import com.example.iexcloud.data.network.response.IEXResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    /*
    * getting the stock info by the symbol
    * */
    @GET("stable/stock/{symbol}/quote?token=Tpk_5b17f588439e43f487de638e708964df")
    suspend fun getStockInfo(@Path("symbol") symbol: String): Response<IEXResponse>
    /*
    * getting the chart info by the symbol
    * */
    @GET("stable/stock/{symbol}/chart/3m?token=Tpk_5b17f588439e43f487de638e708964df")
    suspend fun getChartInfo(@Path("symbol") symbol: String):  Response<IEXChartResponse>
    /*
    * basic retrofit
    * */
    companion object{
        operator fun invoke(): Api{
            val logging = HttpLoggingInterceptor()

            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

            return Retrofit.Builder()
                    //using the sandbox api
                .baseUrl("https://sandbox.iexapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(Api::class.java)
        }
    }
}