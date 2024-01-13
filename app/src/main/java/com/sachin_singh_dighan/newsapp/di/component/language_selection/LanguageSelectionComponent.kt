package com.sachin_singh_dighan.newsapp.di.component.language_selection

import com.sachin_singh_dighan.newsapp.di.ActivityScope
import com.sachin_singh_dighan.newsapp.di.component.ApplicationComponent
import com.sachin_singh_dighan.newsapp.di.module.country_selection.CountrySelectionModule
import com.sachin_singh_dighan.newsapp.di.module.language_selection.LanguageSelectionModule
import com.sachin_singh_dighan.newsapp.ui.country_selection.CountrySelectionActivity
import com.sachin_singh_dighan.newsapp.ui.language_selection.LanguageSelectionActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [LanguageSelectionModule::class])
interface LanguageSelectionComponent {
    fun inject(activity: LanguageSelectionActivity)
}