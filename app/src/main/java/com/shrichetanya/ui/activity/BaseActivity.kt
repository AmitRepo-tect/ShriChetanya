package com.shrichetanya.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.shrichetanya.R
import com.shrichetanya.databinding.DialogSuccessfulLayoutBinding
import com.shrichetanya.kprogresshud.KProgressHUD
import com.shrichetanya.utils.Cutil
import com.shrichetanya.utils.IntentConstant


open class BaseActivity : AppCompatActivity() {
    private var kProgressHUD: KProgressHUD? = null
    fun setStatusBarColor(color: Int) {
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(color, null)
        setSystemNavigationColor()
    }
    private fun setSystemNavigationColor() {
        val window: Window = this.window
        window.navigationBarColor = this.resources.getColor(R.color.white)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
    fun setFullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun getNavigationBarHeight(): Int {
        var navigationBarHeight = 0
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        return navigationBarHeight
    }

    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    open fun showProgressbar() {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
        }
        kProgressHUD?.show()
    }

    open fun hideProgressbar() {
        kProgressHUD?.dismiss()
    }

    fun showCustomDialog(bookingId:Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.confirm_upload_file_dialog_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val title = dialog.findViewById<TextView>(R.id.dialogTitle)
        val yesButton = dialog.findViewById<Button>(R.id.btnYes)
        val noButton = dialog.findViewById<Button>(R.id.btnNo)

        title.text = "Do you want to upload this file?"

        yesButton.setOnClickListener {
            val intent = Intent(this@BaseActivity, UploadFileActivity::class.java)
            intent.putExtra(IntentConstant.BOOKING_ID,bookingId)
            startActivity(intent)
            finish()
        }

        noButton.setOnClickListener {
            showSuccesfulDialog()
            dialog.dismiss()
        }

        dialog.show()
    }
    fun showLogoutDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.confirm_upload_file_dialog_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val title = dialog.findViewById<TextView>(R.id.dialogTitle)
        val yesButton = dialog.findViewById<Button>(R.id.btnYes)
        val noButton = dialog.findViewById<Button>(R.id.btnNo)

        title.text = "Do you want to Logout?"

        yesButton.setOnClickListener {
            Cutil.clearAllPreferences(this@BaseActivity)
            val intent = Intent(this@BaseActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        noButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
   private fun showSaveSuccessfulDialog(

    ): Dialog {
        //val dialog = Dialog(context, R.style.WideDialogTheme)
        val dialog = Dialog(this)
        val binding = DataBindingUtil.inflate<DialogSuccessfulLayoutBinding>(
            LayoutInflater.from(this), R.layout.dialog_successful_layout, null, false
        )
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(binding.root)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation2
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 700)
        dialog.show()

        return dialog
    }

    fun showSuccesfulDialog() {
        val dialog = showSaveSuccessfulDialog()
        dismissDialogAutomatically(dialog)
    }

    private fun dismissDialogAutomatically(dialog: Dialog) {
        Handler(mainLooper).postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
                finish()
            }
        }, 3000)
    }
    fun clearPreference(){

    }
}