package com.sachin_singh_dighan.newsapp.di.module.country_selection

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sachin_singh_dighan.newsapp.data.repository.country_selection.CountrySelectionRepository
import com.sachin_singh_dighan.newsapp.data.repository.main_screen.MainRepository
import com.sachin_singh_dighan.newsapp.di.ActivityContext
import com.sachin_singh_dighan.newsapp.ui.base.ViewModelProviderFactory
import com.sachin_singh_dighan.newsapp.ui.country_selection.CountrySelectionActivity
import com.sachin_singh_dighan.newsapp.ui.country_selection.CountrySelectionAdapter
import com.sachin_singh_dighan.newsapp.ui.country_selection.CountrySelectionViewModel
import com.sachin_singh_dighan.newsapp.ui.main_screen.MainViewModel
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
    fun provideCountrySelectionViewModel(countrySelectionRepository: CountrySelectionRepository): CountrySelectionViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(CountrySelectionViewModel::class) {
                CountrySelectionViewModel(countrySelectionRepository)
            })[CountrySelectionViewModel::class.java]
    }

    @Provides
    fun provideCountrySelectionAdapter() = CountrySelectionAdapter(ArrayList(), activity)

}