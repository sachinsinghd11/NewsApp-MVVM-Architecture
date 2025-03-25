package com.sachin_singh_dighan.newsapp.di

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class BaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabaseName