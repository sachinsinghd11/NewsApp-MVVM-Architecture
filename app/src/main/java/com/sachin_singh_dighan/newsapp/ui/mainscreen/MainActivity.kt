package com.sachin_singh_dighan.newsapp.ui.mainscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sachin_singh_dighan.newsapp.ui.base.NewsNavHost
import com.sachin_singh_dighan.newsapp.ui.theme.NewsAppMVVMArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppMVVMArchitectureTheme {
                NewsNavHost()
            }
        }
    }
}