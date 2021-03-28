package com.example.iexcloud.ui

import android.content.Context
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
import com.example.iexcloud.viewmodel.MainActivityViewModel
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import kotlinx.coroutines.delay
import java.util.ArrayList

class StockDetailFragment: Fragment() {

    private lateinit var binding: StockDetailFragmentBinding
    private val mainViewModel: MainActivityViewModel by activityViewModels()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = StockDetailFragmentBinding.inflate(inflater,container,false)
        binding = fragmentBinding
        mainViewModel.getStock("GME")

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val chartData: IEXChartResponse =  mainViewModel?.getChart(binding.stockSymbol.toString())
        binding.apply {
            mainViewModel?.detailStock?.observe(viewLifecycleOwner, Observer {
                binding.stockSymbol.text = it.symbol
                binding.bidPrice.text = it.iexBidPrice.toString()
                binding.askPrice.text = it.iexAskPrice.toString()
                binding.lastPrice.text = it.lastTradeTime.toString()

            })
            mainViewModel.chartStock.observe(viewLifecycleOwner, Observer {
                candleStick(it)
            })
            //candleStick(viewmodel?.chartResponse!!)
        }

    }
    private fun candleStick(chartData: IEXChartResponse) {


        var candleStickList = ArrayList<CandleEntry>()
        if (chartData.size == 0){
            Log.d("StockDetailFragmanet", "chart is zero")
        }
        var i: Float = 0F
        for (item in chartData) {
            Log.d("StockDetailFragment", "getting chart info: ${item.open}")
            candleStickList.add(
                CandleEntry(
                    i,
                    item.high.toFloat(),
                    item.low.toFloat(),
                    item.open.toFloat(),
                    item.close.toFloat()
                )
            )
            i++
        }
        val candleData: CandleDataSet = CandleDataSet(candleStickList, "DataSet1")
        binding.candleStickChart.data = CandleData(candleData)
        binding.candleStickChart.invalidate()
    }
}