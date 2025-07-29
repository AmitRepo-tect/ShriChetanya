package com.shrichetanya.ui.activity

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ListPopupWindow
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.view.GravityCompat
import com.shrichetanya.R
import com.shrichetanya.databinding.ActDemoLayoutBinding
import com.shrichetanya.databinding.ActHomeLayoutBinding
import com.shrichetanya.model.Client
import com.shrichetanya.ui.adapter.CustomSpinnerAdapter
import com.shrichetanya.ui.adapter.CustomSpinnerAdapterNew
import com.shrichetanya.utils.NetworkResult
import com.shrichetanya.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DemoActivity: BaseActivity(), OnClickListener {
    lateinit var binding: ActDemoLayoutBinding
    val viewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
      /*  window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )*/

        binding = ActDemoLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavigationView()
        setListeners()
        addObservers()
        getClientList()
    }
    private fun getClientList() {
        viewModel.getClientList("")
    }
    private fun setListeners(){
        //binding.menuIv.setOnClickListener(this@DemoActivity)
    }
    private fun setUpNavigationView() {
        /*binding.navView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.new_booking -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START, true)
                    Toast.makeText(this,"New Booking", Toast.LENGTH_SHORT).show()
                }
                R.id.delivery -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START, true)
                    Toast.makeText(this,"Delivery", Toast.LENGTH_SHORT).show()
                }
            }
            menuItem.isChecked = !menuItem.isChecked
            menuItem.isChecked = true
            true
        }
*/
    }

    override fun onClick(v: View) {
  /*      when(v.id){
            R.id.menu_iv -> {
                if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }*/
    }
    fun setSpinnerData(list:List<Client> ){
        /*val topFiveClients: List<Client> = if (list.size >= 5) {
            list.take(5)
        } else {
            list // If fewer than 5, return the whole list
        }*/
        val adapter = CustomSpinnerAdapter(this,list)
        binding.customSpinner.adapter = adapter
        binding.customSpinner.dropDownVerticalOffset = dpToPx(55)

        binding.customSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selected = list[position]
                Toast.makeText(this@DemoActivity, "Selected: $selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional
            }
        }
    }
    private fun addObservers() {

        viewModel.clientResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideProgressbar()
                    response.data?.let {
                        for(i in 0..it.client.size-1){
                            Log.i("Client--","--"+it.client[i])
                        }
                        setSpinnerData(it.client)
                    }
                }

                is NetworkResult.Error -> {
                    hideProgressbar()
                }

                is NetworkResult.Loading -> {
                    showProgressbar()
                }
            }
        }
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}