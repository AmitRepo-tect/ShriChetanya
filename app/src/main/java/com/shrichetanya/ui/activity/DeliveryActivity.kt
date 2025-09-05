package com.shrichetanya.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.shrichetanya.R
import com.shrichetanya.databinding.ActDeliveryLayoutBinding
import com.shrichetanya.model.DeliveryRequest
import com.shrichetanya.utils.Cutil
import com.shrichetanya.utils.IntentConstant
import com.shrichetanya.utils.NetworkResult
import com.shrichetanya.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveryActivity : BaseActivity(), OnClickListener {
    lateinit var binding: ActDeliveryLayoutBinding
    private val viewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActDeliveryLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        addObservers()
        setStatusBarColor(R.color.white)
    }

    private fun setListeners() {
        binding.scanner.setOnClickListener(this)
        binding.submit.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)
        binding.barcodeEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               if(count>0){
                   binding.container.visibility = View.VISIBLE
               }else{
                   binding.container.visibility = View.GONE
               }
            }

            override fun afterTextChanged(s: Editable?) {
                // Called after the text is changed
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.scanner -> {
                val intent = Intent(this, BarcodeScannerActivity::class.java)
                launcher.launch(intent)
            }
            R.id.submit -> {
                deliverBooking()
            }
            R.id.back_btn -> {
                finish()
            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val resultText = data?.getStringExtra(IntentConstant.BARCODE)
                binding.barcodeEt.setText(resultText.toString())
            }
        }

    private fun deliverBooking() {
        if (validate()) {
            viewModel.deliverBooking(
               DeliveryRequest( userId = Cutil.getStringFromSP(this, IntentConstant.USER_ID),
                podNo = binding.barcodeEt.text.toString(),
                receiverName = binding.nameEt.text.toString(),
                receiverMobileNo = binding.mobNumberEt.text.toString(),
                receiverAddress = binding.addressEt.text.toString()
            ))
        }

    }

    private fun addObservers() {
        viewModel.deliverBookingResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideProgressbar()
                    response.data?.let {
                        if (it.result.code == 1) {
                            showCustomDialog(it.bookingId)
                            clearField()
                        } else {
                            Toast.makeText(
                                this@DeliveryActivity,
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
        if (binding.addressEt.text.isEmpty()) {
            Toast.makeText(this, "Please enter Address", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    fun clearField(){
        binding.barcodeEt.text?.clear()
        binding.nameEt.text?.clear()
        binding.mobNumberEt.text?.clear()
        binding.addressEt.text?.clear()
    }
}