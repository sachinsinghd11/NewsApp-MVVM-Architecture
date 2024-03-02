package com.sachin_singh_dighan.newsapp.di.component.countryselection

import com.sachin_singh_dighan.newsapp.di.ActivityScope
import com.sachin_singh_dighan.newsapp.di.component.ApplicationComponent
import com.sachin_singh_dighan.newsapp.di.module.countryselection.CountrySelectionModule
import com.sachin_singh_dighan.newsapp.ui.countryselection.CountrySelectionActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [CountrySelectionModule::class])
interface CountrySelectionComponent {

    fun inject(activity: CountrySelectionActivity)
}