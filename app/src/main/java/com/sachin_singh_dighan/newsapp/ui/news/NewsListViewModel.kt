package com.sachin_singh_dighan.newsapp.ui.news

import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiArticle
import com.sachin_singh_dighan.newsapp.data.repository.topheadline.TopHeadLineRepository
import com.sachin_singh_dighan.newsapp.ui.base.BaseViewModel
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.DispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val topHeadLineRepository: TopHeadLineRepository,
    private val networkHelper: NetworkHelper,
    private val logger: Logger,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<List<ApiArticle>>(networkHelper) {

    companion object {
        const val TAG = "NewsListViewModel"
    }

    fun fetchNewsByCategory(sourceId: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            if (networkHelper.isNetworkAvailable()) {
                topHeadLineRepository.getTopHeadLinesByCategory(sourceId)
                    .flowOn(dispatcherProvider.io)
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

    fun fetchNewsByCountry(country: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            if (networkHelper.isNetworkAvailable()) {
                topHeadLineRepository.getTopHeadLinesByCountry(country)
                    .flowOn(dispatcherProvider.io)
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

    fun fetchNewsByLanguage(language: List<String>) {
        viewModelScope.launch(dispatcherProvider.main) {
            if (networkHelper.isNetworkAvailable()) {
                val selectedLanguagesList = mutableListOf<ApiArticle>()
                topHeadLineRepository.getTopHeadLinesByLanguage(language[0])
                    .zip(topHeadLineRepository.getTopHeadLinesByLanguage(language[1])) { language1, language2 ->
                        val seed = Random.nextInt()
                        selectedLanguagesList.addAll(language1)
                        selectedLanguagesList.addAll(language2)
                        selectedLanguagesList.shuffled(Random(seed))
                    }.flowOn(dispatcherProvider.io)
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