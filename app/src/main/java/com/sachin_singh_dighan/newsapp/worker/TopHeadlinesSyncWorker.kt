package com.sachin_singh_dighan.newsapp.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.R
import com.sachin_singh_dighan.newsapp.data.repository.topheadlinesync.SyncStatus
import com.sachin_singh_dighan.newsapp.data.repository.topheadlinesync.TopHeadlinesSyncRepository
import com.sachin_singh_dighan.newsapp.ui.mainscreen.MainActivity
import com.sachin_singh_dighan.newsapp.utils.DispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext

@HiltWorker
class TopHeadlinesSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TopHeadlinesSyncRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val logger: Logger,
) : CoroutineWorker(context, workerParams) {


    companion object {
        const val TAG = "TopHeadlinesSyncWorker"
    }

    override suspend fun doWork(): Result = withContext(dispatcherProvider.io) {
        try {
            // Perform data sync from API
            val syncResult = repository.syncDataTopHeadlinesFromApi()
            logger.d(TAG, "Sync Worker: Starting work")
            // Return appropriate result based on sync operation
            when (syncResult) {
                SyncStatus.SUCCESS -> {
                    logger.d(TAG, "Sync Worker: Work completed successfully")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        sendNotification()
                    }
                    Result.success()
                }

                SyncStatus.ERROR -> {
                    logger.d(TAG, "Sync Worker: Work failed ERROR")
                    Result.retry()
                }

                SyncStatus.NO_NETWORK -> {
                    logger.d(TAG, "Sync Worker: Work failed NO NETWORK")
                    Result.failure()
                }
            }
        } catch (e: Exception) {
            logger.d(TAG, "Sync Worker: Work failed $e")
            Result.failure()
        }
    }

    @SuppressLint("NewApi")
    private fun sendNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel
        val channel = NotificationChannel(
            AppConstant.NOTIFICATION_CHANNEL_ID,
            AppConstant.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        // Create an Intent for the NewsActivity
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("notification_id", AppConstant.NOTIFICATION_ID)

        // Create a PendingIntent
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notification =
            NotificationCompat.Builder(applicationContext, AppConstant.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(AppConstant.NOTIFICATION_CONTENT_TEXT)
                .setContentTitle(AppConstant.NOTIFICATION_CONTENT_TITLE)
                .setContentIntent(pendingIntent)
                .build()

        // Show the notification
        notificationManager.notify(AppConstant.NOTIFICATION_ID, notification)
    }
}