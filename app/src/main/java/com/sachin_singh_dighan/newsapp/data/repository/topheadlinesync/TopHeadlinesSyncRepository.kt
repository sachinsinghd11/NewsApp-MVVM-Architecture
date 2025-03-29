package com.sachin_singh_dighan.newsapp.data.repository.topheadlinesync

import com.sachin_singh_dighan.newsapp.utils.AppConstant
import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.local.DatabaseService
import com.sachin_singh_dighan.newsapp.data.model.topheadline.toArticleEntity
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


enum class SyncStatus {
    SUCCESS,
    ERROR,
    NO_NETWORK
}

class TopHeadlinesSyncRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService,
    private val networkHelper: NetworkHelper,
    private val logger: Logger,
) {
    companion object {
        const val TAG = "TopHeadlinesSyncRepository"
    }

    // StateFlow to track sync status
    private val _syncStatus = MutableStateFlow(SyncStatus.SUCCESS)
    val syncStatus: Flow<SyncStatus> = _syncStatus.asStateFlow()

    suspend fun syncDataTopHeadlinesFromApi(): SyncStatus {

        return try {
            // Check network connectivity (you might want to inject a network checker)
            if (!networkHelper.isNetworkAvailable()) {
                _syncStatus.update { SyncStatus.NO_NETWORK }
                return SyncStatus.NO_NETWORK
            }

            // Fetch data from API
            val apiArticles = networkService.getTopHeadLinesByCountry(AppConstant.NEWS_BY_DEFAULT)
                .apiArticles
                .map { it.toArticleEntity() }

            // Save data to local database
            databaseService.deleteAllAndInsertAll(apiArticles)

            // Update sync status
            _syncStatus.update { SyncStatus.SUCCESS }
            logger.d(TAG, "WorkManager Diagnostic: Work SUCCEEDED ✅")
            SyncStatus.SUCCESS
        } catch (e: Exception) {
            // Log the error
            _syncStatus.update { SyncStatus.ERROR }
            logger.d(TAG, "WorkManager Diagnostic: Error checking work status $e")
            SyncStatus.ERROR
        }
    }
}