package com.example.iexcloud.ui

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.core.view.contains
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.iexcloud.R
import com.example.iexcloud.data.network.response.IEXResponse
import com.example.iexcloud.data.room.StockEntity
import com.example.iexcloud.databinding.ActivityMainBinding
import com.example.iexcloud.util.Convert
import com.example.iexcloud.viewmodel.MainActivityViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), MainListener {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels<MainActivityViewModel>()
    private val watchListFragment: WatchListFragment = WatchListFragment()
    private val detailFragment: StockDetailFragment = StockDetailFragment()
    private var watchlistLocation = "My first list"
    private lateinit var watchlistLoad: Deferred<List<String>>
    private lateinit var watchList: List<String>
    private lateinit var watchlistMenu: Menu



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.mainListener = this
        binding.viewmodel = viewModel
        val fab = binding.mainFab


        inflateWatchlistFragment()
        viewModel.getStock("GOOG","My first list")
        viewModel.getStock("AAPL","My first list")
        viewModel.getStock("MSFT","My first list")


        fab.setOnClickListener {
            showAlertDialog("Stock Symbol")
        }
        watchlistLoad = GlobalScope.async(Dispatchers.Default){
            viewModel.getWatchlistNames()
        }



    }

    private fun inflateWatchlistFragment(){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frame_container,watchListFragment)
            .commit()
    }

    override fun OnSuccess() {
    }

    /*
    * shows the dialog box when adding the stock
    * TODO add the auto complete
    * */
    private fun showAlertDialog(type: String){
        val builder = AlertDialog.Builder(this)
        // Get the layout inflater
        val inflater = layoutInflater.inflate(R.layout.dialog_add_stock,null)
        val etSymbol = inflater.findViewById<EditText>(R.id.et_symbol)
        etSymbol.hint = type

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater)
                // Add action buttons
                .setPositiveButton("ADD",
                        DialogInterface.OnClickListener { dialog, id ->
                            // checking if the stock symbol needs the alertDialog
                            if (etSymbol.text.toString() != "" && etSymbol.hint == "Stock Symbol")
                                viewModel.getStock(etSymbol.text.toString(),watchlistLocation)
                            //checking if the watchlist needs the alertDialog
                            if (etSymbol.hint == "Watchlist name")
                                watchlistMenu.add(etSymbol.text.toString())
                        })
                .setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, id ->
                            //TODO exit out dialog
                        })
        builder.create().show()
    }

    fun inflateDetailedView(dataItem: StockEntity) {
        viewModel.setDetailStock(dataItem)
        //binding.mainFab.visibility
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_container,detailFragment)
                .addToBackStack(null)
                .commit()


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        watchlistMenu = menu!!
        menuInflater.inflate(R.menu.navigation_items,menu)

        //making the watchlist persistent not ordered well
        //TODO order list
        //TODO add Add watchlist to a sub menu so it always shows up at the bottom
        GlobalScope.launch(Dispatchers.Default){
            watchList = watchlistLoad.await()
            for (item in watchList.distinct()){
                if (item != "My first list")
                    menu.add(item)
            }
        }

        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title.toString() == "Add Watchlist"){
            Snackbar.make(binding.root,"This is a simple Snackbar",Snackbar.LENGTH_LONG).show()
            showAlertDialog("Watchlist name")
        }
        else{
            viewModel.getWatchlist(item.title.toString())
            watchlistLocation = item.title.toString()
            //replacing the fragment to reload it
            supportFragmentManager.beginTransaction()
                .detach(watchListFragment)
                .attach(watchListFragment).commit()
        }
        return super.onOptionsItemSelected(item)
    }



}