package com.sachin_singh_dighan.newsapp.ui.searchnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
import com.sachin_singh_dighan.newsapp.data.repository.searchnews.SearchNewsRepository
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SearchNewsViewModel(
    private val searchNewsRepository: SearchNewsRepository,
    private val networkHelper: NetworkHelper,
    private val logger: Logger,
) : ViewModel() {

    companion object {
        const val TAG = "SearchNewsViewModel"
    }

    private val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<Article>>> = _uiState

    private val query = MutableStateFlow("")

    init {
        getQueryTextChangeStateFlow()
    }

    fun searchQuery(searchQuery: String) {
        query.value = searchQuery
    }

    private fun getQueryTextChangeStateFlow() {
        viewModelScope.launch {
            if (networkHelper.isNetworkAvailable()) {
                query.debounce(2000)
                    .filter {
                        if (it.isNotEmpty() && it.count() >= 3) {
                            return@filter true
                        } else {
                            _uiState.value = UiState.Success(emptyList())
                            return@filter false
                        }

                    }
                    .distinctUntilChanged()
                    .flatMapLatest { searchTerm ->
                        _uiState.value = UiState.Loading
                        return@flatMapLatest searchNewsRepository.getSearchForHeadLines(searchTerm)
                            .catch { e ->
                                _uiState.value = UiState.Error(e.toString())
                                logger.d(TAG, e.toString())
                            }
                    }
                    .flowOn(Dispatchers.IO)
                    .collect {
                        if (it.isNotEmpty()) {
                            _uiState.value = UiState.Success(it)
                            logger.d(TAG, it.toString())
                        } else {
                            _uiState.value = UiState.Error(AppConstant.NO_DATA_FOUND)
                            logger.d(TAG, AppConstant.NO_DATA_FOUND)
                        }
                    }
            } else {
                _uiState.value = UiState.Error(AppConstant.NETWORK_ERROR)
            }

        }
    }
}