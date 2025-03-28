package com.sachin_singh_dighan.newsapp.ui.topheadlinesync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.worker.TopHeadlineSyncScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopHeadlineSyncViewModel @Inject constructor(
    private val topHeadlineSyncScheduler: TopHeadlineSyncScheduler,
) : ViewModel() {
    // Expose sync status from repository
    //val syncStatus: Flow<SyncStatus> = topHeadlineSyncRepository.syncStatus

    fun scheduleDailySync() {
        viewModelScope.launch {
            topHeadlineSyncScheduler.scheduleDailySync()
        }
    }

    //Manual sync trigger
    fun scheduleOneTimeSync() {
        viewModelScope.launch {
            topHeadlineSyncScheduler.startOneTimeRequest()
        }
    }
}