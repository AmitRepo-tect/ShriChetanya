package com.shrichetanya.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.shrichetanya.R
import com.shrichetanya.databinding.ActNewBookingLayoutBinding
import com.shrichetanya.model.BookingRequest
import com.shrichetanya.utils.Cutil
import com.shrichetanya.utils.IntentConstant
import com.shrichetanya.utils.NetworkResult
import com.shrichetanya.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewBookingActivity : BaseActivity(), OnClickListener {
    lateinit var binding: ActNewBookingLayoutBinding
    private val viewModel by viewModels<HomeViewModel>()
    private var clinetId: Int = -1
    lateinit var selectedMod:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActNewBookingLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        addObservers()
        getDataFromIntent()
        setSpinnerData()
        setStatusBarColor(R.color.white)
    }

    private fun getDataFromIntent() {
        clinetId = intent.getIntExtra(IntentConstant.CLIENT_ID, -1) // Default -1 if not found
    }

    private fun setListeners() {
        binding.scanner.setOnClickListener(this)
        binding.submit.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.scanner -> {
                val intent = Intent(this, BarcodeScannerActivity::class.java)
                launcher.launch(intent)
            }

            R.id.submit -> {
                saveBooking()
            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val resultText = data?.getStringExtra(IntentConstant.BARCODE)
                binding.barcodeEt.setText(resultText.toString())
                binding.container.visibility=View.VISIBLE
            }
        }

    private fun saveBooking() {
        if (validate()) {
            viewModel.saveBooking(
                BookingRequest(userId = Cutil.getStringFromSP(this, IntentConstant.USER_ID),
                clinetId = clinetId.toString(),
                podNo = binding.barcodeEt.text.toString(),
                isDocument = binding.radioYes.isChecked,
                mode = selectedMod,
                itemName = binding.itemNameEt.text.toString(),
                itemQuantity = binding.itemQuantityEt.text.toString(),
                itemValue = binding.itemValueEt.text.toString(),
                sourceAddress = binding.sourceAddressEt.text.toString(),
                sourcePinCode = binding.sourcePinEt.text.toString(),
                destinationAddress = binding.destAddressEt.text.toString(),
                destinationPinCode = binding.destAddressEt.text.toString(),
                bookingName = binding.nameEt.text.toString(),
                bookingMobileNo = binding.mobNumberEt.text.toString()
            ))
        }

    }

    private fun addObservers() {

        viewModel.saveBookingResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideProgressbar()
                    response.data?.let {
                        if (it.result.code == 1) {
                            showCustomDialog(it.bookingId)
                        }else{
                            Toast.makeText(
                                this@NewBookingActivity,
                                it.result.msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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

    private fun validate(): Boolean {
        if (binding.barcodeEt.text.isEmpty()) {
            Toast.makeText(this, "Please enter POD", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.nameEt.text.isEmpty()) {
            Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.mobNumberEt.text.isEmpty()) {
            Toast.makeText(this, "Please enter Mobile", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.sourceAddressEt.text.isEmpty()) {
            Toast.makeText(this, "Please enter Source Address", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.sourcePinEt.text.isEmpty()) {
            Toast.makeText(this, "Please enter Pin", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.destAddressEt.text.isEmpty()) {
            Toast.makeText(this, "Please enter Destination Address", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.destPinEt.text.isEmpty()) {
            Toast.makeText(this, "Please enter Pin", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.itemNameEt.text.isEmpty()) {
            Toast.makeText(this, "Please enter Item Name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.itemValueEt.text.isEmpty()) {
            Toast.makeText(this, "Please enter Item Value", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.itemQuantityEt.text.isEmpty()) {
            Toast.makeText(this, "Please enter Item Quantity", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
    private fun setSpinnerData(){
        val options = listOf("Surface","Express","Air")

        // Adapter for Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.customSpinner.adapter = adapter

        // Handle selection
        binding.customSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                selectedMod=options[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // do nothing
            }
        }
    }
}