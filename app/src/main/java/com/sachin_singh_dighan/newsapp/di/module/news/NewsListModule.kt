package com.sachin_singh_dighan.newsapp.di.module.news

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sachin_singh_dighan.newsapp.data.repository.countryselection.CountrySelectionRepository
import com.sachin_singh_dighan.newsapp.data.repository.news.NewsListRepository
import com.sachin_singh_dighan.newsapp.data.repository.topheadline.TopHeadLineRepository
import com.sachin_singh_dighan.newsapp.di.ActivityContext
import com.sachin_singh_dighan.newsapp.ui.base.ViewModelProviderFactory
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.news.NewsListAdapter
import com.sachin_singh_dighan.newsapp.ui.news.NewsListViewModel
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadLineAdapter
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadLineViewModel
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import dagger.Module
import dagger.Provides

@Module
class NewsListModule(private val activity: AppCompatActivity) {

    @ActivityContext
    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun providesNewsListViewModel(
        newsListRepository: NewsListRepository,
        topHeadLineRepository: TopHeadLineRepository,
        networkHelper: NetworkHelper,
    ): NewsListViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(NewsListViewModel::class) {
                NewsListViewModel(
                    newsListRepository,
                    topHeadLineRepository,
                    networkHelper,
                )
            }
        )[NewsListViewModel::class.java]
    }

    @Provides
    fun provideTopHeadLineAdapter() = NewsListAdapter(ArrayList())

    @Provides
    fun provideErrorDialog() = ErrorDialog()
}