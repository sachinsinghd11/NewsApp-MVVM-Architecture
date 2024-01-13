package com.sachin_singh_dighan.newsapp.di.module.language_selection

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.sachin_singh_dighan.newsapp.data.repository.language_selection.LanguageSelectionRepository
import com.sachin_singh_dighan.newsapp.di.ActivityContext
import com.sachin_singh_dighan.newsapp.ui.base.ViewModelProviderFactory
import com.sachin_singh_dighan.newsapp.ui.language_selection.LanguageSelectionActivity
import com.sachin_singh_dighan.newsapp.ui.language_selection.LanguageSelectionAdapter
import com.sachin_singh_dighan.newsapp.ui.language_selection.LanguageSelectionViewModel
import dagger.Module
import dagger.Provides

@Module
class LanguageSelectionModule(private val activity: LanguageSelectionActivity) {

    @ActivityContext
    @Provides
    fun provideContext(): Context{
        return activity
    }

    @Provides
    fun provideLanguageSelectionViewModel(languageSelectionRepository: LanguageSelectionRepository): LanguageSelectionViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(LanguageSelectionViewModel::class) {
                LanguageSelectionViewModel(languageSelectionRepository)
            })[LanguageSelectionViewModel::class.java]
    }

    @Provides
    fun provideCountrySelectionAdapter() = LanguageSelectionAdapter(ArrayList(), activity)

}