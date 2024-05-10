package com.sachin_singh_dighan.newsapp.di.module.countryselection

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.sachin_singh_dighan.newsapp.data.repository.countryselection.CountrySelectionRepository
import com.sachin_singh_dighan.newsapp.di.ActivityContext
import com.sachin_singh_dighan.newsapp.ui.common.ViewModelProviderFactory
import com.sachin_singh_dighan.newsapp.ui.countryselection.CountrySelectionActivity
import com.sachin_singh_dighan.newsapp.ui.countryselection.CountrySelectionAdapter
import com.sachin_singh_dighan.newsapp.ui.countryselection.CountrySelectionViewModel
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import dagger.Module
import dagger.Provides

@Module
class CountrySelectionModule(private val activity: CountrySelectionActivity) {

    @ActivityContext
    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideCountrySelectionViewModel(
        countrySelectionRepository: CountrySelectionRepository,
        networkHelper: NetworkHelper,
    ): CountrySelectionViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(CountrySelectionViewModel::class) {
                CountrySelectionViewModel(countrySelectionRepository, networkHelper)
            })[CountrySelectionViewModel::class.java]
    }

    @Provides
    fun provideCountrySelectionAdapter() = CountrySelectionAdapter(ArrayList(), activity)

    @Provides
    fun provideErrorDialog() = ErrorDialog()
}