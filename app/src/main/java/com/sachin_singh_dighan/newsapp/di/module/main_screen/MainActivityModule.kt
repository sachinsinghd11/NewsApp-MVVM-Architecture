package com.sachin_singh_dighan.newsapp.di.module.main_screen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sachin_singh_dighan.newsapp.data.repository.main_screen.MainRepository
import com.sachin_singh_dighan.newsapp.di.ActivityContext
import com.sachin_singh_dighan.newsapp.ui.base.ViewModelProviderFactory
import com.sachin_singh_dighan.newsapp.ui.main_screen.MainActivityAdapter
import com.sachin_singh_dighan.newsapp.ui.main_screen.MainViewModel
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule(private val activity: AppCompatActivity) {

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
    fun provideMainActivityAdapter() = MainActivityAdapter(ArrayList())
}