package com.sachin_singh_dighan.newsapp.di.component.top_headline

import com.sachin_singh_dighan.newsapp.di.ActivityScope
import com.sachin_singh_dighan.newsapp.di.component.ApplicationComponent
import com.sachin_singh_dighan.newsapp.di.module.top_headline.TopHeadLineModule
import com.sachin_singh_dighan.newsapp.ui.top_headline.TopHeadLineActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [TopHeadLineModule::class])
interface TopHeadLineComponent {
    fun inject(activity: TopHeadLineActivity)
}