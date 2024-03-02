package com.sachin_singh_dighan.newsapp.di.module.search_news

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sachin_singh_dighan.newsapp.data.repository.search_news.SearchNewsRepository
import com.sachin_singh_dighan.newsapp.data.repository.top_headline.TopHeadLineRepository
import com.sachin_singh_dighan.newsapp.di.ActivityContext
import com.sachin_singh_dighan.newsapp.ui.base.ViewModelProviderFactory
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.search_news.SearchNewsViewModel
import com.sachin_singh_dighan.newsapp.ui.top_headline.TopHeadLineAdapter
import com.sachin_singh_dighan.newsapp.ui.top_headline.TopHeadLineViewModel
import dagger.Module
import dagger.Provides

@Module
class SearchNewsModule(private val activity: AppCompatActivity) {
    @ActivityContext
    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun providesSearchNewsViewModel(searchNewsRepository: SearchNewsRepository): SearchNewsViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(SearchNewsViewModel::class){
                SearchNewsViewModel(searchNewsRepository)
            }
        )[SearchNewsViewModel::class.java]
    }

    @Provides
    fun provideTopHeadLineAdapter() = TopHeadLineAdapter(ArrayList())

    @Provides
    fun provideErrorDialog() = ErrorDialog()
}