package com.sachin_singh_dighan.newsapp.di.component.languageselection

import com.sachin_singh_dighan.newsapp.di.ActivityScope
import com.sachin_singh_dighan.newsapp.di.component.ApplicationComponent
import com.sachin_singh_dighan.newsapp.di.module.languageselection.LanguageSelectionModule
import com.sachin_singh_dighan.newsapp.ui.languageselection.LanguageSelectionActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [LanguageSelectionModule::class])
interface LanguageSelectionComponent {
    fun inject(activity: LanguageSelectionActivity)
}