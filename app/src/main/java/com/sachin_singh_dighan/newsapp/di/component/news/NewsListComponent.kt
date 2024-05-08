package com.sachin_singh_dighan.newsapp.di.component.news

import com.sachin_singh_dighan.newsapp.di.ActivityScope
import com.sachin_singh_dighan.newsapp.di.component.ApplicationComponent
import com.sachin_singh_dighan.newsapp.di.module.news.NewsListModule
import com.sachin_singh_dighan.newsapp.ui.news.NewsListActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [NewsListModule::class])
interface NewsListComponent {
    fun inject(activity: NewsListActivity)
}