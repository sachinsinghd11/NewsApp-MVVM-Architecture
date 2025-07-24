package com.sachin_singh_dighan.newsapp

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.sachin_singh_dighan.newsapp.utils.AppConstant
import com.sachin_singh_dighan.newsapp.utils.EncryptedApiKeyManager
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

    override fun onCreate() {
        super.onCreate()
        initializeEncryptedStorage()
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

    /**
     * Initialize encrypted storage for API keys
     */
    private fun initializeEncryptedStorage() {
        try {
            val apiKeyManager = EncryptedApiKeyManager(this)
            apiKeyManager.initializeIfNeeded()
            Log.d("NewsApplication", "Encrypted storage initialized successfully")
        } catch (e: Exception) {
            Log.e("NewsApplication", "Failed to initialize encrypted storage: ${e.message}")
        }
    }

}