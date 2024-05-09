package com.sachin_singh_dighan.newsapp.di.component

import android.content.Context
import com.sachin_singh_dighan.newsapp.NewsApplication
import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.repository.mainscreen.MainRepository
import com.sachin_singh_dighan.newsapp.data.repository.topheadline.TopHeadLineRepository
import com.sachin_singh_dighan.newsapp.di.ApplicationContext
import com.sachin_singh_dighan.newsapp.di.module.ApplicationModule
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(newsApplication: NewsApplication)

    @ApplicationContext
    fun provideContext() : Context

    fun provideMainRepository() : MainRepository

    fun getNetworkService(): NetworkService

    fun getTopHeadLineRepository(): TopHeadLineRepository

    fun getNetworkHelper(): NetworkHelper


}