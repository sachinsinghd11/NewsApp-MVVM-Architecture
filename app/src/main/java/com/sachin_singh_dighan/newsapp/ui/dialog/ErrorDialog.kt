package com.sachin_singh_dighan.newsapp.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.sachin_singh_dighan.newsapp.databinding.ActivityMainBinding
import com.sachin_singh_dighan.newsapp.databinding.ActivityTopHeadLineBinding
import com.sachin_singh_dighan.newsapp.databinding.ErrorMessagePopupBinding

class ErrorDialog {

    private lateinit var binding:ErrorMessagePopupBinding
    fun showResetPasswordDialog(activity: Activity?, errorMessage: String,) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        binding = ErrorMessagePopupBinding.inflate(LayoutInflater.from(activity))
        dialog.setContentView(binding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.errorMessage.text = errorMessage
        binding.btnTryAgain.setOnClickListener {
            dialog.dismiss()
            activity.finish()
        }
        dialog.show()
    }
}