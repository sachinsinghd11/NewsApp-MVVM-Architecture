package com.sachin_singh_dighan.newsapp.ui.newsources

import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.newsources.Sources
import com.sachin_singh_dighan.newsapp.data.repository.newsources.NewSourcesRepository
import com.sachin_singh_dighan.newsapp.ui.base.BaseViewModel
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsSourcesViewModel @Inject constructor(
    private val newSourcesRepository: NewSourcesRepository,
    private val networkHelper: NetworkHelper,
    private val logger: Logger,
) : BaseViewModel<List<Sources>>(networkHelper) {

    companion object {
        const val TAG = "NewsSourcesViewModel"
    }

    init {
        fetchNewSource()
    }

    private fun fetchNewSource() {
        viewModelScope.launch {
            if (networkHelper.isNetworkAvailable()) {
                newSourcesRepository.getNewResources()
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        handleError(e)
                        _uiState.value = UiState.Error(e.toString())
                        logger.d(TAG, e.toString())
                    }.collect {
                        _uiState.value = UiState.Success(it)
                        logger.d(TAG, it.toString())
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
        fetchNewSource()
    }
}