package com.sachin_singh_dighan.newsapp.di.component.country_selection

import com.sachin_singh_dighan.newsapp.di.ActivityScope
import com.sachin_singh_dighan.newsapp.di.component.ApplicationComponent
import com.sachin_singh_dighan.newsapp.di.module.country_selection.CountrySelectionModule
import com.sachin_singh_dighan.newsapp.di.module.main_screen.MainActivityModule
import com.sachin_singh_dighan.newsapp.ui.country_selection.CountrySelectionActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [CountrySelectionModule::class])
interface CountrySelectionComponent {

    fun inject(activity: CountrySelectionActivity)
}