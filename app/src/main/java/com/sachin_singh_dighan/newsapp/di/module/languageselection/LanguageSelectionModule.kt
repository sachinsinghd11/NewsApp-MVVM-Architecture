package com.sachin_singh_dighan.newsapp.di.module.languageselection

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.sachin_singh_dighan.newsapp.data.repository.languageselection.LanguageSelectionRepository
import com.sachin_singh_dighan.newsapp.di.ActivityContext
import com.sachin_singh_dighan.newsapp.ui.common.ViewModelProviderFactory
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.languageselection.LanguageSelectionActivity
import com.sachin_singh_dighan.newsapp.ui.languageselection.LanguageSelectionAdapter
import com.sachin_singh_dighan.newsapp.ui.languageselection.LanguageSelectionViewModel
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import dagger.Module
import dagger.Provides

@Module
class LanguageSelectionModule(private val activity: LanguageSelectionActivity) {

    @ActivityContext
    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideLanguageSelectionViewModel(
        languageSelectionRepository: LanguageSelectionRepository,
        networkHelper: NetworkHelper,
    ): LanguageSelectionViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(LanguageSelectionViewModel::class) {
                LanguageSelectionViewModel(languageSelectionRepository, networkHelper,)
            })[LanguageSelectionViewModel::class.java]
    }

    @Provides
    fun provideCountrySelectionAdapter() = LanguageSelectionAdapter(ArrayList(), activity)

    @Provides
    fun provideErrorDialog() = ErrorDialog()
}