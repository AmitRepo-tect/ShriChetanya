package com.shrichetanya.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler

import android.view.animation.Animation
import android.view.animation.AnimationUtils

import com.shrichetanya.R
import com.shrichetanya.databinding.ActSplashLayoutBinding
import com.shrichetanya.utils.Cutil
import com.shrichetanya.utils.SharePreferenceConstant
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    lateinit var binding: ActSplashLayoutBinding

    private fun attachViewModel() {
        if (Cutil.getBooleanFromSP(this, SharePreferenceConstant.IS_LOGGED_IN)) {
            Handler().postDelayed({
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        } else {
            Handler().postDelayed({
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActSplashLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animateImageView()
        attachViewModel()
        setStatusBarColor(R.color.white)
    }

    private fun animateImageView() {
        val aniSlide: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom_in)
        binding.logoIv.startAnimation(aniSlide)
    }
}