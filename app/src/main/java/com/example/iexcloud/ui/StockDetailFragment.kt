package com.example.iexcloud.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.iexcloud.R
import com.example.iexcloud.data.network.response.IEXChartResponse
import com.example.iexcloud.data.room.StockEntity
import com.example.iexcloud.databinding.StockDetailFragmentBinding
import com.example.iexcloud.util.Coroutines
import com.example.iexcloud.viewmodel.MainActivityViewModel
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import kotlinx.coroutines.*
import java.util.ArrayList

class StockDetailFragment: Fragment() {

    private lateinit var binding: StockDetailFragmentBinding
    private val mainViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var data: IEXChartResponse
    lateinit var dataLoad: Deferred<IEXChartResponse>


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = StockDetailFragmentBinding.inflate(inflater,container,false)
        binding = fragmentBinding

        dataLoad = GlobalScope.async(Dispatchers.Default) {
            val sym: String= mainViewModel?.detailStock?.value?.symbol!!
            mainViewModel?.getChart(sym)
        }
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.Default){
            data = dataLoad.await()
            candleStick(data)
        }

        binding.apply {
            mainViewModel?.detailStock?.observe(viewLifecycleOwner, Observer {
                binding.stockSymbol.text = it.symbol
                binding.bidPrice.text = it.iexBidPrice.toString()
                binding.askPrice.text = it.iexAskPrice.toString()
                binding.lastPrice.text = it.lastTradeTime.toString()

            })
        }

    }
    private fun candleStick(chartData: IEXChartResponse) {
        Log.d("StockDetailFragmanet", "candle stick")
        var candleStickList = ArrayList<CandleEntry>()
        if (chartData.size == 0){
            Log.d("StockDetailFragmanet", "chart is zero")
        }
        var i: Float = 0F
        for (item in chartData) {
            candleStickList.add(
                CandleEntry(
                    i,
                    item.high.toFloat(),
                    item.low.toFloat(),
                    item.open.toFloat(),
                    item.close.toFloat()
                )
            )
            if(i == 30F)
                break
            i++
        }
        val candleData: CandleDataSet = CandleDataSet(candleStickList, chartData[0].symbol)
        candleData.color = Color.rgb(80,80,80)
        candleData.shadowColorSameAsCandle = true
        candleData.shadowWidth = 0.8F
        candleData.decreasingColor = resources.getColor(R.color.red)
        candleData.decreasingPaintStyle = Paint.Style.FILL
        candleData.increasingColor = resources.getColor(R.color.green)
        candleData.increasingPaintStyle = Paint.Style.FILL
        candleData.setDrawValues(false)
        binding.candleStickChart.data = CandleData(candleData)
        binding.candleStickChart.invalidate()
    }
}