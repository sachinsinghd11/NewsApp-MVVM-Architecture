package com.sachin_singh_dighan.newsapp.di.module

import com.sachin_singh_dighan.newsapp.ui.countryselection.CountrySelectionAdapter
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.languageselection.LanguageSelectionAdapter
import com.sachin_singh_dighan.newsapp.ui.mainscreen.MainActivityAdapter
import com.sachin_singh_dighan.newsapp.ui.news.NewsListAdapter
import com.sachin_singh_dighan.newsapp.ui.newsources.NewsSourcesAdapter
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadLineAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    fun provideCountrySelectionAdapter() = CountrySelectionAdapter(ArrayList())

    @Provides
    fun provideLanguageSelectionAdapter() = LanguageSelectionAdapter(ArrayList())

    @Provides
    fun provideMainActivityAdapter() = MainActivityAdapter(ArrayList())

    @Provides
    fun provideNewsListAdapter() = NewsListAdapter(ArrayList())

    @Provides
    fun provideNewSourceAdapter() = NewsSourcesAdapter(ArrayList())

    @Provides
    fun provideTopHeadLineAdapter() = TopHeadLineAdapter(ArrayList())

    @Provides
    fun provideErrorDialog() = ErrorDialog()
}