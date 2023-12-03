package com.sachin_singh_dighan.newsapp

import android.app.Application
import com.sachin_singh_dighan.newsapp.di.component.ApplicationComponent
import com.sachin_singh_dighan.newsapp.di.component.DaggerApplicationComponent
import com.sachin_singh_dighan.newsapp.di.module.ApplicationModule

class NewsApplication: Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        injectDependencies()
        super.onCreate()
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
                .build()
        applicationComponent.inject(this)

    }
}