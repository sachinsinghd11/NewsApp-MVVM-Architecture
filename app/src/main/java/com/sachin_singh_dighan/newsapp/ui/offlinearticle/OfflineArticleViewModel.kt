package com.sachin_singh_dighan.newsapp.ui.offlinearticle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.local.entity.Article
import com.sachin_singh_dighan.newsapp.data.repository.offlineArticles.OfflineArticleRepository
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.DispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineArticleViewModel @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val offlineArticleRepository: OfflineArticleRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    protected val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<Article>>> = _uiState

    init {
        if (networkHelper.isNetworkAvailable()) {
            fetchArticles()
        } else {
            fetchArticlesDirectFromDB()
        }
    }

    private fun fetchArticles() {
        viewModelScope.launch(dispatcherProvider.main) {
            offlineArticleRepository.getArticles(AppConstant.NEWS_BY_DEFAULT)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }


    private fun fetchArticlesDirectFromDB() {
        viewModelScope.launch(dispatcherProvider.main) {
            offlineArticleRepository.getArticlesDirectlyFromDB()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

}