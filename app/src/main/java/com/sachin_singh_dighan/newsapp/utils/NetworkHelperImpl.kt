package com.sachin_singh_dighan.newsapp.utils

import android.content.Context
import android.net.ConnectivityManager

class NetworkHelperImpl(private val context: Context): NetworkHelper {
    override fun isNetworkAvailable(): Boolean {
        val connectivityManager =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkStatus = connectivityManager.activeNetworkInfo
        return networkStatus?.isConnected?:false
    }

}