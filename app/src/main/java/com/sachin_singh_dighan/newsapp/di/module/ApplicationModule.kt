package com.sachin_singh_dighan.newsapp.di.module

import android.content.Context
import com.sachin_singh_dighan.newsapp.NewsApplication
import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.di.ApplicationContext
import com.sachin_singh_dighan.newsapp.di.BaseUrl
import com.sachin_singh_dighan.newsapp.utils.AuthInterceptor
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.NetworkHelperImpl
import com.sachin_singh_dighan.newsapp.utils.RetryInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: NewsApplication) {

    @ApplicationContext
    @Provides
    fun provideContext(): Context {
        return application
    }

    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = "https://newsapi.org/v2/"

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideNetworkService(
        @BaseUrl baseUrl: String,
        gsonConverterFactory: GsonConverterFactory
    ): NetworkService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(AuthInterceptor())
            .addInterceptor(loggingInterceptor).addInterceptor(RetryInterceptor(3)).build()
        return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(gsonConverterFactory)
            .client(client).build().create(NetworkService::class.java)
    }


    @Provides
    @Singleton
    fun provideNetworkHelper(@ApplicationContext context: Context): NetworkHelper{
        return NetworkHelperImpl(context)
    }

}