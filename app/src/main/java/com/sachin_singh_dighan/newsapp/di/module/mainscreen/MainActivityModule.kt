package com.sachin_singh_dighan.newsapp.di.module.mainscreen

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.sachin_singh_dighan.newsapp.data.repository.mainscreen.MainRepository
import com.sachin_singh_dighan.newsapp.di.ActivityContext
import com.sachin_singh_dighan.newsapp.ui.base.ViewModelProviderFactory
import com.sachin_singh_dighan.newsapp.ui.mainscreen.MainActivity
import com.sachin_singh_dighan.newsapp.ui.mainscreen.MainActivityAdapter
import com.sachin_singh_dighan.newsapp.ui.mainscreen.MainViewModel
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule(private val activity: MainActivity) {

    @ActivityContext
    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideMainViewModel(mainRepository: MainRepository): MainViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(MainViewModel::class) {
            MainViewModel(mainRepository)
        })[MainViewModel::class.java]
    }


    @Provides
    fun provideMainActivityAdapter() = MainActivityAdapter(ArrayList(), activity)
}