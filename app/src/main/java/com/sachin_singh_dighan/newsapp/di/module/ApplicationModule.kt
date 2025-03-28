package com.sachin_singh_dighan.newsapp.di.module

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.local.AppDatabase
import com.sachin_singh_dighan.newsapp.data.local.AppDatabaseService
import com.sachin_singh_dighan.newsapp.data.local.DatabaseService
import com.sachin_singh_dighan.newsapp.di.BaseUrl
import com.sachin_singh_dighan.newsapp.di.DatabaseName
import com.sachin_singh_dighan.newsapp.utils.AuthInterceptor
import com.sachin_singh_dighan.newsapp.utils.DefaultDispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.DispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.NetworkHelperImpl
import com.sachin_singh_dighan.newsapp.utils.RetryInterceptor
import com.sachin_singh_dighan.newsapp.utils.logger.AppLogger
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

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
    fun provideNetworkHelper(@ApplicationContext context: Context): NetworkHelper {
        return NetworkHelperImpl(context)
    }

    @Provides
    @Singleton
    fun provideLogger(): Logger {
        return AppLogger()
    }

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @DatabaseName
    @Provides
    fun provideDatabaseName(): String = "news_database"


    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        @DatabaseName databaseName: String
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            databaseName
        ).build()
    }

    @Provides
    @Singleton
    fun provideDatabaseService(appDatabase: AppDatabase): DatabaseService {
        return AppDatabaseService(appDatabase)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    /*@Provides
    @Singleton
    fun provideTopHeadlineSyncScheduler(
        @ApplicationContext context: Context,
    ): TopHeadlineSyncScheduler {
        return TopHeadlineSyncScheduler(context)
    }*/

}

