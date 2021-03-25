package com.example.iexcloud.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.iexcloud.R
import com.example.iexcloud.data.network.response.IEXResponse

class WatchListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val watchListAdapter: WatchListAdapter = WatchListAdapter(mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_watchlist_rv,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.watchlist_rv)
        recyclerView.adapter = watchListAdapter
    }

    fun updateWatchList(response: List<IEXResponse>){
        watchListAdapter.dataset = response
        watchListAdapter.notifyDataSetChanged()
    }
}