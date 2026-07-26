package com.sachin_singh_dighan.newsapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.sachin_singh_dighan.newsapp.utils.AppConstant
import com.sachin_singh_dighan.newsapp.utils.SecurityConfig
import com.sachin_singh_dighan.newsapp.utils.TimeUtil
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import com.sachin_singh_dighan.newsapp.worker.TopHeadlinesSyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class NewsApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var securityConfig: SecurityConfig
    
    @Inject
    lateinit var appLogger: Logger

    override fun onCreate() {
        super.onCreate()
        appLogger.d("NewsApplication", "Application onCreate started")

        try {
            // Initialize security configuration
            val securityInitialized = securityConfig.initializeSecurity()

            if (securityInitialized) {
                appLogger.d("NewsApplication", "Security configuration initialized successfully")
            } else {
                appLogger.e("NewsApplication", "Failed to initialize security configuration")
            }

        } catch (e: Exception) {
            appLogger.e("NewsApplication", "Error during security initialization: ${e.message}")
        }

        appLogger.d("NewsApplication", "Application onCreate completed")
        initWorkManager()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    private fun initWorkManager() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequest.Builder(
            TopHeadlinesSyncWorker::class.java,
            24,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInitialDelay(TimeUtil.getInitialDelay(), TimeUnit.MILLISECONDS)
            .addTag(AppConstant.UNIQUE_WORK_NAME)
            .build()

        workManager.enqueueUniquePeriodicWork(
            AppConstant.UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, workRequest
        )
    }

}