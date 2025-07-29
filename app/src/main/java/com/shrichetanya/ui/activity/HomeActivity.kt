package com.shrichetanya.ui.activity

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import com.shrichetanya.utils.NetworkResult
import com.shrichetanya.R
import com.shrichetanya.databinding.ActHomeLayoutBinding
import com.shrichetanya.model.Client
import com.shrichetanya.ui.adapter.CustomSpinnerAdapterNew
import com.shrichetanya.utils.IntentConstant
import com.shrichetanya.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity(), OnClickListener {
    lateinit var binding: ActHomeLayoutBinding
    val viewModel by viewModels<HomeViewModel>()
    var boolval: Boolean = false
    lateinit var client: MutableList<Client>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActHomeLayoutBinding.inflate(layoutInflater)
        client= mutableListOf()
        setContentView(binding.root)
        setUpNavigationView()
        setListeners()
        addObservers()
        getClientList()
        setStatusBarColor(R.color.white)
    }

    private fun getClientList() {
        viewModel.getClientList("")
    }

    private fun setListeners() {
        binding.menuIv.setOnClickListener(this@HomeActivity)
    }

    private fun setUpNavigationView() {
        binding.navView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.new_booking -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START, true)
                }

                R.id.delivery -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START, true)
                    val intent = Intent(this@HomeActivity, DeliveryActivity::class.java)
                    startActivity(intent)
                }
                R.id.logout->{
                    showLogoutDialog()
                    binding.drawerLayout.closeDrawer(GravityCompat.START, true)

                }
            }
            menuItem.isChecked = !menuItem.isChecked
            menuItem.isChecked = true
            true
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.menu_iv -> {
                if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }
    }

    private fun setSpinnerData(list: List<Client>) {

        val adapter = CustomSpinnerAdapterNew(this, list)
        binding.customSpinner.adapter = adapter
        binding.customSpinner.dropDownVerticalOffset = dpToPx(55)


        binding.customSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                if (position!=0) {
                    val intent = Intent(this@HomeActivity, NewBookingActivity::class.java)
                    intent.putExtra(IntentConstant.CLIENT_ID, list[position].id)
                    startActivity(intent)
                }
                boolval = true
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
                        client.add(0,Client(0,"Select Client"))
                        client.addAll(it.client)
                        setSpinnerData(client)
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