package com.example.iexcloud.ui

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.iexcloud.R
import com.example.iexcloud.data.room.StockEntity
import com.example.iexcloud.databinding.ActivityMainBinding
import com.example.iexcloud.viewmodel.MainActivityViewModel
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
        //setting up view binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.mainListener = this
        binding.viewmodel = viewModel
        val fab = binding.mainFab


        inflateWatchlistFragment()

        //adding the 3 main stocks to the default list
        //TODO check in the database to see if it has info in it
        viewModel.addStock("GOOG","My first list")
        viewModel.addStock("AAPL","My first list")
        viewModel.addStock("MSFT","My first list")

        //setting a click listener for add stock feature
        fab.setOnClickListener {
            showAlertDialog("Stock Symbol")
        }
        //getting the names of the watchlist to pass them to the menu
        watchlistLoad = GlobalScope.async(Dispatchers.Default){
            viewModel.getWatchlistNames()
        }



    }
/*
* inflate the main watchlist fragment*/
    private fun inflateWatchlistFragment(){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frame_container,watchListFragment)
            .commit()
    }

    override fun OnSuccess() {
    }

    /*
    * shows the dialog box when adding the stock or adding a watchlist
    * TODO add the auto complete
    * */
    private fun showAlertDialog(type: String){
        val builder = AlertDialog.Builder(this)
        // Get the layout inflater
        val inflater = layoutInflater.inflate(R.layout.dialog_add_stock,null)
        val etSymbol = inflater.findViewById<EditText>(R.id.et_symbol)
        etSymbol.hint = type

        builder.setView(inflater)
                .setPositiveButton("ADD",
                        DialogInterface.OnClickListener { dialog, id ->
                            // checking if the stock symbol needs the alertDialog
                            if (etSymbol.text.toString() != "" && etSymbol.hint == "Stock Symbol")
                                viewModel.addStock(etSymbol.text.toString(),watchlistLocation)
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
/*
* inflating the detailed view
* pass the stock you want the detailed view on
* */
    fun inflateDetailedView(dataItem: StockEntity) {
        viewModel.setDetailStock(dataItem)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_container,detailFragment)
                .addToBackStack(null)
                .commit()


    }


    /*
    * creating the options menu for the watchlists
    * */
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
    /*
    * when a item on the watchlist menu is selected
    * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //adding a watchlist to the menu
        if (item.title.toString() == "Add Watchlist"){
            showAlertDialog("Watchlist name")
        }
        else{
            //changing the watchlist to the one selected
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