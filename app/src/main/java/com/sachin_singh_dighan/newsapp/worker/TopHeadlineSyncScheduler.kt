package com.sachin_singh_dighan.newsapp.worker

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sachin_singh_dighan.newsapp.AppConstant
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopHeadlineSyncScheduler @Inject constructor(
    private val workManager: WorkManager,
) {

    fun scheduleDailySync() {
        // Create constraints for the sync
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            //.setRequiresBatteryNotLow(true)
            .build()

        // Calculate delay to next 5 Am
        val delay = calculateDelayTo5Am()

        // Create periodic work request
        val syncWorkRequest = PeriodicWorkRequestBuilder<TopHeadlinesSyncWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        // Enqueue the work
        workManager.enqueueUniquePeriodicWork(
            "daily_data_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncWorkRequest
        )
    }

    private fun calculateDelayTo5Am(): Long {
        val currentTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, AppConstant.MORNING_UPDATE_TIME)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // If the target time has already passed today, move to next day
        if (currentTime.after(targetTime)) {
            targetTime.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Calculate delay
        return targetTime.timeInMillis - currentTime.timeInMillis
    }

    fun startOneTimeRequest() {
        val constraints = Constraints.Builder()
            .build()

        val data = Data.Builder()
            .putString("channel_id", "one_time_work")
            .putString("title", "One Time Request")
            .putString("message", "This notification is from One Time Request")
            .build()

        val oneTimeRequest = OneTimeWorkRequest.Builder(TopHeadlinesSyncWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        workManager.enqueue(oneTimeRequest)
    }
}