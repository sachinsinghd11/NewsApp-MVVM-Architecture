package com.sachin_singh_dighan.newsapp.di.module.top_headline

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sachin_singh_dighan.newsapp.data.repository.top_headline.TopHeadLineRepository
import com.sachin_singh_dighan.newsapp.di.ActivityContext
import com.sachin_singh_dighan.newsapp.ui.base.ViewModelProviderFactory
import com.sachin_singh_dighan.newsapp.ui.top_headline.TopHeadLineAdapter
import com.sachin_singh_dighan.newsapp.ui.top_headline.TopHeadLineViewModel
import dagger.Module
import dagger.Provides

@Module
class TopHeadLineModule(private val activity: AppCompatActivity) {

    @ActivityContext
    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun providesNewsListViewModel(topHeadLineRepository: TopHeadLineRepository): TopHeadLineViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(TopHeadLineViewModel::class){
                TopHeadLineViewModel(topHeadLineRepository)
            }
        )[TopHeadLineViewModel::class.java]
    }

    @Provides
    fun provideTopHeadLineAdapter() = TopHeadLineAdapter(ArrayList())
}