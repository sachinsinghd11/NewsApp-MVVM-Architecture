package com.sachin_singh_dighan.newsapp.ui.searchnews

import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
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
import kotlinx.coroutines.flow.StateFlow
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
) : BaseViewModel<List<*>>(networkHelper) {

    companion object {
        const val TAG = "SearchNewsViewModel"
    }


    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    fun setQueryTextChangeStateFlow(searchFlow: StateFlow<String>) {
        viewModelScope.launch {
            searchFlow.debounce(2000)
                .filter {
                    if (it.isNotEmpty() && it.count() >= 3) {
                        return@filter true
                    } else {
                        _uiState.value = UiState.Success(emptyList<Article>())
                        return@filter false
                    }
                }
                .distinctUntilChanged()
                .flatMapLatest { searchTerm ->
                    _uiState.value = UiState.Loading
                    return@flatMapLatest searchNewsRepository.getSearchForHeadLines(searchTerm)
                        .catch { e ->
                            logger.d(TAG, e.toString())
                            _uiState.value = UiState.Error(e.toString())
                        }
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    if (networkHelper.isNetworkAvailable() && it.isNotEmpty()) {
                        logger.d(TAG, it.toString())
                        _uiState.value = UiState.Success(it)
                    } else {
                        logger.d(TAG, AppConstant.NO_DATA_FOUND)
                        _uiState.value = UiState.Error(AppConstant.NO_DATA_FOUND)
                    }
                }
        }
    }
}