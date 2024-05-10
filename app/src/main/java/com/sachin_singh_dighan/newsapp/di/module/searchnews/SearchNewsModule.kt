package com.sachin_singh_dighan.newsapp.di.module.searchnews

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sachin_singh_dighan.newsapp.data.repository.searchnews.SearchNewsRepository
import com.sachin_singh_dighan.newsapp.di.ActivityContext
import com.sachin_singh_dighan.newsapp.ui.common.ViewModelProviderFactory
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.searchnews.SearchNewsViewModel
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadLineAdapter
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
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
    fun providesSearchNewsViewModel(
        searchNewsRepository: SearchNewsRepository,
        networkHelper: NetworkHelper
    ): SearchNewsViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(SearchNewsViewModel::class) {
                SearchNewsViewModel(searchNewsRepository, networkHelper)
            }
        )[SearchNewsViewModel::class.java]
    }

    @Provides
    fun provideTopHeadLineAdapter() = TopHeadLineAdapter(ArrayList())

    @Provides
    fun provideErrorDialog() = ErrorDialog()
}