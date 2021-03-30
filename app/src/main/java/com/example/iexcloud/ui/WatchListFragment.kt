package com.example.iexcloud.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.iexcloud.R
import com.example.iexcloud.data.room.StockEntity
import com.example.iexcloud.viewmodel.MainActivityViewModel

class WatchListFragment : Fragment(), WatchListAdapter.StockClick {
    private lateinit var recyclerView: RecyclerView
    private lateinit var activityHost: MainActivity
    private val watchListAdapter: WatchListAdapter = WatchListAdapter(mutableListOf(),this)
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            activityHost = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_watchlist_rv,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Recycler View setup
        recyclerView = view.findViewById(R.id.watchlist_rv) //TODO change to view binding
        recyclerView.adapter = watchListAdapter

        //Live data setup  to keep an eye on the watchlist so you can add data to recycler view
        mainActivityViewModel.watchListData.observe(viewLifecycleOwner, Observer {
            watchListAdapter.dataset = it
            watchListAdapter.notifyDataSetChanged()
        })

    }
    /*
    * when an item is clicked this will get the stock info for the detailed view
    * */
    override fun getInfo(data: StockEntity) {
        activityHost.inflateDetailedView(data)
    }
    /*
    * whenever a long click happens on a stock a stock will get deleted
    * */
    override fun deleteInfo(data: StockEntity) {
        //TODO maybe add a alert dialog. to confirm delete
        mainActivityViewModel.deleteStock(data)
    }


}