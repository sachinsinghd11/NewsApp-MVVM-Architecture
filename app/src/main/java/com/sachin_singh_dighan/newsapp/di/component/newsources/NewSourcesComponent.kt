package com.sachin_singh_dighan.newsapp.di.component.newsources

import com.sachin_singh_dighan.newsapp.di.ActivityScope
import com.sachin_singh_dighan.newsapp.di.component.ApplicationComponent
import com.sachin_singh_dighan.newsapp.di.module.newsources.NewSourcesModule
import com.sachin_singh_dighan.newsapp.ui.newsources.NewsSourcesActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [NewSourcesModule::class])
interface NewSourcesComponent {
    fun inject(activity: NewsSourcesActivity)
}