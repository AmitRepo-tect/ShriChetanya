package com.shrichetanya.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.Toast
import com.shrichetanya.R
import com.shrichetanya.databinding.ActLoginBinding
import androidx.activity.viewModels
import com.shrichetanya.model.LoginRequest
import com.shrichetanya.utils.Cutil
import com.shrichetanya.utils.IntentConstant
import com.shrichetanya.utils.NetworkResult
import com.shrichetanya.utils.SharePreferenceConstant
import com.shrichetanya.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity(), OnClickListener {

    lateinit var binding: ActLoginBinding
    val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding = ActLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        addObservers()
    }

    private fun setListeners() {
        binding.loginBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_btn -> {
                if(validate()){
                    login()
                }
            }
            else -> println("x is something else")
        }
    }

    private fun login() {
        viewModel.login(LoginRequest(binding.phoneNumberEt.text.toString(), binding.passwordEt.text.toString()) )
    }

    private fun validate(): Boolean {
        if (binding.phoneNumberEt.text.isEmpty()) {
            Toast.makeText(this@LoginActivity, "Please Enter Mobile Number", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (binding.passwordEt.text.isEmpty()) {
            Toast.makeText(this@LoginActivity, "Please Enter Password", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun addObservers() {

        viewModel.loginResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                   hideProgressbar()
                    response.data?.let {
                        Cutil.saveStringInSP(this,SharePreferenceConstant.USER_ID,it.userId.toString())
                        Cutil.saveStringInSP(this,SharePreferenceConstant.USER_NAME, it.name)
                        Cutil.saveStringInSP(this,SharePreferenceConstant.USER_PASSWORD,binding.passwordEt.text.toString())
                        Cutil.saveStringInSP(this,SharePreferenceConstant.USER_EMAIL, it.emailId)
                        Cutil.saveBooleanInSP(this,SharePreferenceConstant.IS_LOGGED_IN,true)
                        Cutil.saveStringInSP(this,SharePreferenceConstant.AUTH_KEY,it.authKey)

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
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
}