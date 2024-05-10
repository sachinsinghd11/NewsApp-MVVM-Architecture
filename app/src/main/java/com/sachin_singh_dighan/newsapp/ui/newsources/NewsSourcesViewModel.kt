package com.sachin_singh_dighan.newsapp.ui.newsources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.newsources.Sources
import com.sachin_singh_dighan.newsapp.data.repository.newsources.NewSourcesRepository
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class NewsSourcesViewModel(
    private val newSourcesRepository: NewSourcesRepository,
    private val networkHelper: NetworkHelper,
    private val logger: Logger,
) : ViewModel() {

    companion object {
        const val TAG = "NewsSourcesViewModel"
    }

    private val _uiState = MutableStateFlow<UiState<List<Sources>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<Sources>>> = _uiState

    init {
        fetchNewSource()
    }

    private fun fetchNewSource() {
        viewModelScope.launch {
            if (networkHelper.isNetworkAvailable()) {
                newSourcesRepository.getNewResources()
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        _uiState.value = UiState.Error(e.toString())
                        logger.d(TAG, e.toString())
                    }.collect {
                        _uiState.value = UiState.Success(it)
                        logger.d(TAG, it.toString())
                    }
            } else {
                _uiState.value = UiState.Error(AppConstant.NETWORK_ERROR)
            }

        }
    }
}