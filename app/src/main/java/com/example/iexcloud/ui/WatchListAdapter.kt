package com.example.iexcloud.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iexcloud.data.network.response.IEXResponse
import com.example.iexcloud.data.room.StockEntity
import com.example.iexcloud.databinding.WatchlistItemLayoutBinding
import com.google.android.material.snackbar.Snackbar

class WatchListAdapter(var dataset: List<StockEntity>, val stockClick: StockClick) : RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder>(){
    /*
    * interface for the onclicks
    * */
    interface StockClick{
        fun getInfo(data: StockEntity)
        fun deleteInfo(data: StockEntity)
    }

    /*
    * Adapter stuff for the recycler view
    * */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchListViewHolder {
        val binding = WatchlistItemLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchListViewHolder(binding)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: WatchListViewHolder, position: Int) {
        holder.onBind(dataset[position])
    }
    inner class WatchListViewHolder(val binding : WatchlistItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root){

        private val stockSymbol: TextView = binding.stockSymbol
        private val bidPrice: TextView = binding.bidPrice
        private val askPrice: TextView = binding.askPrice
        private val lastPrice: TextView = binding.lastPrice

        fun onBind(data: StockEntity){
            //click listeners for each item in the RV
            binding.root.setOnClickListener {
                stockClick.getInfo(data)
            }
            binding.root.setOnLongClickListener {
                stockClick.deleteInfo(data)
                true
            }

            stockSymbol.text = data.symbol
            bidPrice.text = data.iexBidPrice.toString()
            askPrice.text = data.iexAskPrice.toString()
            lastPrice.text = data.latestPrice.toString()
        }
    }
}