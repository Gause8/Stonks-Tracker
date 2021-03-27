package com.example.iexcloud.ui

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.iexcloud.R
import com.example.iexcloud.data.network.response.IEXResponse
import com.example.iexcloud.data.room.StockEntity
import com.example.iexcloud.databinding.ActivityMainBinding
import com.example.iexcloud.util.Convert
import com.example.iexcloud.viewmodel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), MainListener {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels<MainActivityViewModel>()
    private val watchListFragment: WatchListFragment = WatchListFragment()
    private val stockList = ArrayList<IEXResponse>()
    private var isFabVis = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.mainListener = this
        binding.viewmodel = viewModel
        val fab = binding.mainFab
        inflateWatchlistFragment()
        binding.viewmodel?.getStock("GOOG")
        binding.viewmodel?.getStock("AAPL")
        viewModel.getStock("MSFT")

        fab.setOnClickListener {
            if (isFabVis)
                visibilityOff()
            else
                visibilityOn()
        }
        binding.addFab.setOnClickListener {
            showAlertDialog()

        }
        //TODO delete might be better for long click on recyler view
        binding.deletFab.setOnClickListener {
            Snackbar.make(it, "deleting stock", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
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
      * Checking if the addition add buttons are visable
      * might delete later*/
    private fun visibilityOn(){
        binding.addFab.visibility = View.VISIBLE
        binding.deletFab.visibility = View.VISIBLE
        isFabVis = true
    }
    /*
    * Checking if the addition add buttons visibilty are off
    * might delete later*/
    private fun visibilityOff(){
        binding.addFab.visibility = View.GONE
        binding.deletFab.visibility = View.GONE
        isFabVis = false
    }

    /*
    * shows the dialog box when adding the stock
    * TODO add the auto complete
    * */
    private fun showAlertDialog(){
        val builder = AlertDialog.Builder(this)
        // Get the layout inflater
        val inflater = layoutInflater.inflate(R.layout.dialog_add_stock,null)
        val etSymbol = inflater.findViewById<EditText>(R.id.et_symbol)

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater)
                // Add action buttons
                .setPositiveButton("ADD",
                        DialogInterface.OnClickListener { dialog, id ->
                            // add stock
                            if (etSymbol.text.toString() != null)
                                viewModel.getStock(etSymbol.text.toString())
                        })
                .setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, id ->
                            //TODO exit out dialog
                        })
        builder.create().show()
    }

}