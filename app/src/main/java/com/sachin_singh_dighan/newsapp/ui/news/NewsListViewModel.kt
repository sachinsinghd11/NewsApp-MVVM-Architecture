package com.sachin_singh_dighan.newsapp.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
import com.sachin_singh_dighan.newsapp.data.repository.news.NewsListRepository
import com.sachin_singh_dighan.newsapp.data.repository.topheadline.TopHeadLineRepository
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class NewsListViewModel(
    private val newsListRepository: NewsListRepository,
    private val topHeadLineRepository: TopHeadLineRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<Article>>> = _uiState

    fun fetchNewsByResource(sourceId: String) {
        viewModelScope.launch {
            if (networkHelper.isNetworkAvailable()) {
                newsListRepository.getNewsListByResources(sourceId)
                    .catch { e ->
                        _uiState.value = UiState.Error(e.toString())
                    }.collect {
                        _uiState.value = UiState.Success(it)
                    }
            } else {
                _uiState.value = UiState.Error(AppConstant.NETWORK_ERROR)
            }

        }
    }

    fun fetchNewsByCountry(country: String) {
        viewModelScope.launch {
            if (networkHelper.isNetworkAvailable()) {
                topHeadLineRepository.getTopHeadLinesByCountry(country)
                    .catch { e ->
                        _uiState.value = UiState.Error(e.toString())
                    }.collect {
                        _uiState.value = UiState.Success(it)
                    }
            } else {
                _uiState.value = UiState.Error(AppConstant.NETWORK_ERROR)
            }

        }
    }

    fun fetchNewsByLanguage(language: String) {
        viewModelScope.launch {
            if (networkHelper.isNetworkAvailable()) {
                topHeadLineRepository.getTopHeadLinesByLanguage(language)
                    .catch { e ->
                        _uiState.value = UiState.Error(e.toString())
                    }.collect {
                        _uiState.value = UiState.Success(it)
                    }
            } else {
                _uiState.value = UiState.Error(AppConstant.NETWORK_ERROR)
            }

        }
    }
}