package com.example.iexcloud.util

import com.example.iexcloud.data.network.response.IEXResponse
import com.example.iexcloud.data.room.StockEntity

object Convert {
    fun entityToResponse(stock: StockEntity):IEXResponse
        = IEXResponse(stock.avgTotalVolume,
                stock.calculationPrice,stock.change,stock.changePercent,stock.closeSource,
                stock.companyName,stock.iexAskPrice,stock.iexAskSize,stock.iexBidPrice,
                stock.iexBidSize,stock.iexClose,stock.iexCloseTime,stock.iexLastUpdated,
                stock.iexMarketPercent,stock.iexOpen,stock.iexOpenTime,stock.iexRealtimePrice,
                stock.iexRealtimeSize,stock.iexVolume,stock.isUSMarketOpen,stock.lastTradeTime,
                stock.latestPrice,stock.latestSource,stock.latestTime,stock.latestUpdate,
                stock.marketCap,stock.openSource,stock.previousClose,stock.previousVolume,
                stock.primaryExchange,stock.symbol,stock.week52High,stock.week52Low,stock.ytdChange)

    /*
    * convert the api response to a entity for the DB
    * */
    fun responseToEntity(stock: IEXResponse, watchListName: String):StockEntity
        = StockEntity(stock.avgTotalVolume,
                stock.calculationPrice,stock.change,stock.changePercent,stock.closeSource,
                stock.companyName,stock.iexAskPrice,stock.iexAskSize,stock.iexBidPrice,
                stock.iexBidSize,stock.iexClose,stock.iexCloseTime,stock.iexLastUpdated,
                stock.iexMarketPercent,stock.iexOpen,stock.iexOpenTime,stock.iexRealtimePrice,
                stock.iexRealtimeSize,stock.iexVolume,stock.isUSMarketOpen,stock.lastTradeTime,
                stock.latestPrice,stock.latestSource,stock.latestTime,stock.latestUpdate,
                stock.marketCap,stock.openSource,stock.previousClose,stock.previousVolume,
                stock.primaryExchange,stock.symbol,stock.week52High,stock.week52Low,stock.ytdChange,
                watchListName)

}