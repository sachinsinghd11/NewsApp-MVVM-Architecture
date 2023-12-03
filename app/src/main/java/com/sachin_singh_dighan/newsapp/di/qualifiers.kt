package com.sachin_singh_dighan.newsapp.di

import javax.inject.Qualifier
import javax.inject.Scope


@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class ApplicationContext

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class ActivityContext
