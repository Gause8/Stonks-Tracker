package com.example.iexcloud.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.iexcloud.R
import com.example.iexcloud.data.network.response.IEXResponse
import com.example.iexcloud.viewmodel.MainActivityViewModel

class WatchListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val watchListAdapter: WatchListAdapter = WatchListAdapter(mutableListOf())
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

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

        //Live data setup
        Log.d("Fragment", "updating...")
        mainActivityViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            Log.d("Fragment", it[0].symbol)
            watchListAdapter.dataset = it
            watchListAdapter.notifyDataSetChanged()
        })
    }
}