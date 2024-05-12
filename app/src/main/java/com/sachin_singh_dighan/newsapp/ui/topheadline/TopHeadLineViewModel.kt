package com.sachin_singh_dighan.newsapp.ui.topheadline

import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.repository.topheadline.TopHeadLineRepository
import com.sachin_singh_dighan.newsapp.ui.base.BaseViewModel
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopHeadLineViewModel @Inject constructor(
    private val topHeadLineRepository: TopHeadLineRepository,
    private val networkHelper: NetworkHelper,
    private val logger: Logger,
) : BaseViewModel<List<*>>(networkHelper) {

    companion object {
        const val TAG = "TopHeadLineViewModel"
    }

    init {
        fetchTopHeadlines()
    }

    private fun fetchTopHeadlines() {
        viewModelScope.launch {
            if (networkHelper.isNetworkAvailable()) {
                topHeadLineRepository.getTopHeadLinesByDefault()
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        logger.d(TAG, e.toString())
                        _uiState.value = UiState.Error(e.toString())
                    }.collect {
                        logger.d(TAG, it.toString())
                        _uiState.value = UiState.Success(it)
                    }
            } else {
                _uiState.value = UiState.Error(AppConstant.NETWORK_ERROR)
            }
        }
    }
}