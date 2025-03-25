package com.sachin_singh_dighan.newsapp.ui.searchnews

import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiArticle
import com.sachin_singh_dighan.newsapp.data.repository.searchnews.SearchNewsRepository
import com.sachin_singh_dighan.newsapp.ui.base.BaseViewModel
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val searchNewsRepository: SearchNewsRepository,
    private val networkHelper: NetworkHelper,
    private val logger: Logger,
) : BaseViewModel<List<ApiArticle>>(networkHelper) {

    companion object {
        const val TAG = "SearchNewsViewModel"
    }

    // SearchQuery state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery

    init {
        setQueryTextChangeStateFlow()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _uiState.value = UiState.Success(emptyList())
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    fun setQueryTextChangeStateFlow() {
        logger.d("SearchCompose", "searchFlow = ${searchQuery.value}")
        viewModelScope.launch {
            searchQuery.debounce(2000)
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
                            handleError(e)
                            logger.d(TAG, e.toString())
                            _uiState.value = UiState.Error(e.toString())
                        }
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    if (networkHelper.isNetworkAvailable() && it.isNotEmpty()) {
                        logger.d(TAG, it.toString())
                        _uiState.value = UiState.Success(it)
                        logger.d("SearchCompose", "it.size = ${it.size}")
                    } else {
                        logger.d(TAG, AppConstant.NO_DATA_FOUND)
                        _uiState.value = UiState.Error(AppConstant.NO_DATA_FOUND)
                    }
                }
        }
    }

    // Retry the operation after failure
    fun retryOperation() {
        _showErrorDialog.value = false
        setQueryTextChangeStateFlow()
    }
}