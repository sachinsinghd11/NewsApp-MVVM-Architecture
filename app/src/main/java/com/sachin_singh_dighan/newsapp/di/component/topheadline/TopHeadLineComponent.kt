package com.sachin_singh_dighan.newsapp.di.component.topheadline

import com.sachin_singh_dighan.newsapp.di.ActivityScope
import com.sachin_singh_dighan.newsapp.di.component.ApplicationComponent
import com.sachin_singh_dighan.newsapp.di.module.topheadline.TopHeadLineModule
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadLineActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [TopHeadLineModule::class])
interface TopHeadLineComponent {
    fun inject(activity: TopHeadLineActivity)
}