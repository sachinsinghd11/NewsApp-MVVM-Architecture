package com.sachin_singh_dighan.newsapp.di.component.mainscreen

import com.sachin_singh_dighan.newsapp.di.ActivityScope
import com.sachin_singh_dighan.newsapp.di.component.ApplicationComponent
import com.sachin_singh_dighan.newsapp.di.module.mainscreen.MainActivityModule
import com.sachin_singh_dighan.newsapp.ui.mainscreen.MainActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [MainActivityModule::class])
interface MainActivityComponent {

    fun inject(activity: MainActivity)
}