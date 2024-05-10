package com.sachin_singh_dighan.newsapp.di.module.topheadline

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sachin_singh_dighan.newsapp.data.repository.topheadline.TopHeadLineRepository
import com.sachin_singh_dighan.newsapp.di.ActivityContext
import com.sachin_singh_dighan.newsapp.ui.common.ViewModelProviderFactory
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadLineAdapter
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadLineViewModel
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
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
    fun providesNewsListViewModel(
        topHeadLineRepository: TopHeadLineRepository,
        networkHelper: NetworkHelper,
        logger: Logger,
    ): TopHeadLineViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(TopHeadLineViewModel::class) {
                TopHeadLineViewModel(topHeadLineRepository, networkHelper, logger)
            }
        )[TopHeadLineViewModel::class.java]
    }

    @Provides
    fun provideTopHeadLineAdapter() = TopHeadLineAdapter(ArrayList())

    @Provides
    fun provideErrorDialog() = ErrorDialog()
}