package com.sachin_singh_dighan.newsapp.di.module.newsources

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.sachin_singh_dighan.newsapp.data.repository.newsources.NewSourcesRepository
import com.sachin_singh_dighan.newsapp.di.ActivityContext
import com.sachin_singh_dighan.newsapp.ui.base.ViewModelProviderFactory
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.newsources.NewsSourcesActivity
import com.sachin_singh_dighan.newsapp.ui.newsources.NewSourcesAdapter
import com.sachin_singh_dighan.newsapp.ui.newsources.NewSourcesViewModel
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
    ): NewSourcesViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(NewSourcesViewModel::class) {
                NewSourcesViewModel(newSourcesRepository, networkHelper)
            }
        )[NewSourcesViewModel::class.java]
    }

    @Provides
    fun provideNewSourceAdapter() = NewSourcesAdapter(ArrayList(), activity)

    @Provides
    fun provideErrorDialog() = ErrorDialog()
}