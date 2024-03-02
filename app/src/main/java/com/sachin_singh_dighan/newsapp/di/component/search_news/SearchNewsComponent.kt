package com.sachin_singh_dighan.newsapp.di.component.search_news

import androidx.appcompat.app.AppCompatActivity
import com.sachin_singh_dighan.newsapp.di.ActivityScope
import com.sachin_singh_dighan.newsapp.di.component.ApplicationComponent
import com.sachin_singh_dighan.newsapp.di.module.search_news.SearchNewsModule
import com.sachin_singh_dighan.newsapp.ui.search_news.SearchNewsActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [SearchNewsModule::class])
interface SearchNewsComponent {
    fun inject(activity: SearchNewsActivity)
}