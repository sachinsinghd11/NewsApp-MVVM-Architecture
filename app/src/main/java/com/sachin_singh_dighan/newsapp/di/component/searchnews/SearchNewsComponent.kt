package com.sachin_singh_dighan.newsapp.di.component.searchnews

import com.sachin_singh_dighan.newsapp.di.ActivityScope
import com.sachin_singh_dighan.newsapp.di.component.ApplicationComponent
import com.sachin_singh_dighan.newsapp.di.module.searchnews.SearchNewsModule
import com.sachin_singh_dighan.newsapp.ui.searchnews.SearchNewsActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [SearchNewsModule::class])
interface SearchNewsComponent {
    fun inject(activity: SearchNewsActivity)
}