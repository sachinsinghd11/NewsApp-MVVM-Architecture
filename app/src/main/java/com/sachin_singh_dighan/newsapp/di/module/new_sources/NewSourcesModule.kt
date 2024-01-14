package com.sachin_singh_dighan.newsapp.di.module.new_sources

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.sachin_singh_dighan.newsapp.data.repository.new_sources.NewSourcesRepository
import com.sachin_singh_dighan.newsapp.di.ActivityContext
import com.sachin_singh_dighan.newsapp.ui.base.ViewModelProviderFactory
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.new_sources.NewSourcesActivity
import com.sachin_singh_dighan.newsapp.ui.new_sources.NewSourcesAdapter
import com.sachin_singh_dighan.newsapp.ui.new_sources.NewSourcesViewModel
import dagger.Module
import dagger.Provides

@Module
class NewSourcesModule(private val activity: NewSourcesActivity) {

    @ActivityContext
    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun providesNewSourceViewModel(newSourcesRepository: NewSourcesRepository): NewSourcesViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(NewSourcesViewModel::class){
                NewSourcesViewModel(newSourcesRepository)
            }
        )[NewSourcesViewModel::class.java]
    }

    @Provides
    fun provideNewSourceAdapter() = NewSourcesAdapter(ArrayList(), activity)

    @Provides
    fun provideErrorDialog() = ErrorDialog()
}