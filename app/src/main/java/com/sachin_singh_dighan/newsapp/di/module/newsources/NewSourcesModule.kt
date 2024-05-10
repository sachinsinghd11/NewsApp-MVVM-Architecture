package com.sachin_singh_dighan.newsapp.di.module.newsources

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.sachin_singh_dighan.newsapp.data.repository.newsources.NewSourcesRepository
import com.sachin_singh_dighan.newsapp.di.ActivityContext
import com.sachin_singh_dighan.newsapp.ui.common.ViewModelProviderFactory
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.newsources.NewsSourcesActivity
import com.sachin_singh_dighan.newsapp.ui.newsources.NewsSourcesAdapter
import com.sachin_singh_dighan.newsapp.ui.newsources.NewsSourcesViewModel
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import dagger.Module
import dagger.Provides

@Module
class NewSourcesModule(private val activity: NewsSourcesActivity) {

    @ActivityContext
    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun providesNewSourceViewModel(
        newSourcesRepository: NewSourcesRepository,
        networkHelper: NetworkHelper,
    ): NewsSourcesViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(NewsSourcesViewModel::class) {
                NewsSourcesViewModel(newSourcesRepository, networkHelper)
            }
        )[NewsSourcesViewModel::class.java]
    }

    @Provides
    fun provideNewSourceAdapter() = NewsSourcesAdapter(ArrayList(), activity)

    @Provides
    fun provideErrorDialog() = ErrorDialog()
}