package com.sachin_singh_dighan.newsapp.ui.topheadline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.data.model.top_headline.Article
import com.sachin_singh_dighan.newsapp.data.repository.top_headline.TopHeadLineRepository
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TopHeadLineViewModel(private val topHeadLineRepository: TopHeadLineRepository): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<Article>>> = _uiState

    fun fetchNews(country: String,language: String = ""){
        viewModelScope.launch {
            topHeadLineRepository.getTopHeadLines(country, language)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }.collect{
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}