package com.example.iexcloud.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import com.example.iexcloud.R
import com.example.iexcloud.data.network.response.IEXResponse
import com.example.iexcloud.databinding.ActivityMainBinding
import com.example.iexcloud.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity(), MainListener {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels<MainActivityViewModel>()
    private val watchListFragment: WatchListFragment = WatchListFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.mainListener = this
        binding.viewmodel = viewModel
        binding.viewmodel?.getStock("GOOG")
        binding.viewmodel?.getStock("AAPL")
        binding.viewmodel?.getStock("MSFT")




        inflateWatchlistFragment()

        //setContentView(R.layout.activity_main)
    }

    private fun inflateWatchlistFragment(){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frame_container,watchListFragment)
            .commit()
    }

    override fun OnSuccess(response: List<IEXResponse>) {
        watchListFragment.updateWatchList(response)
    }

}