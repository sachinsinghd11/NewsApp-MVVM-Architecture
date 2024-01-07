package com.sachin_singh_dighan.newsapp.di.component.new_sources

import com.sachin_singh_dighan.newsapp.di.ActivityScope
import com.sachin_singh_dighan.newsapp.di.component.ApplicationComponent
import com.sachin_singh_dighan.newsapp.di.module.new_sources.NewSourcesModule
import com.sachin_singh_dighan.newsapp.ui.new_sources.NewSourcesActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [NewSourcesModule::class])
interface NewSourcesComponent {
    fun inject(activity: NewSourcesActivity)
}