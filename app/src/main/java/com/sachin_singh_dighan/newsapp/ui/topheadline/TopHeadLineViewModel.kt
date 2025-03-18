package com.sachin_singh_dighan.newsapp.ui.topheadline

import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
import com.sachin_singh_dighan.newsapp.data.repository.topheadline.TopHeadLineRepository
import com.sachin_singh_dighan.newsapp.ui.base.BaseViewModel
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class TopHeadLineViewModel @Inject constructor(
    private val topHeadLineRepository: TopHeadLineRepository,
    private val networkHelper: NetworkHelper,
    private val logger: Logger,
) : BaseViewModel<List<Article>>(networkHelper) {

    companion object {
        const val TAG = "TopHeadLineViewModel"
    }

    /*// Track if an error dialog is showing
    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog

    // Store error message
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage*/

    init {
        fetchTopHeadlines()
    }

    private fun fetchTopHeadlines() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if (networkHelper.isNetworkAvailable()) {
                topHeadLineRepository.getTopHeadLinesByDefault()
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        handleError(e)
                        logger.d(TAG, e.toString())
                        _uiState.value = UiState.Error(e.toString())
                    }.collect {
                        logger.d(TAG, it.toString())
                        _uiState.value = UiState.Success(it)
                    }
            } else {
                _uiState.value = UiState.Error(AppConstant.NETWORK_ERROR)
                handleError(IOException("Network error. Please check your connection."))
            }
        }
    }

    // Retry the operation after failure
    fun retryOperation() {
        _showErrorDialog.value = false
        fetchTopHeadlines()
    }

}