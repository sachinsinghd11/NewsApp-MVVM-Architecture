package com.sachin_singh_dighan.newsapp.ui.search_news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.data.model.top_headline.Article
import com.sachin_singh_dighan.newsapp.data.repository.search_news.SearchNewsRepository
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SearchNewsViewModel(private val searchNewsRepository: SearchNewsRepository): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<Article>>> = _uiState

    private val isSearchTextEntered = MutableLiveData<Boolean>()


    fun setIsSearchTextEntered(searchStatus: Boolean){
        isSearchTextEntered.value = searchStatus
    }

    fun getIsSearchTextEntered(): Boolean?{
        return isSearchTextEntered.value
    }

    fun fetchSearchResult(searchTerm: String){
        viewModelScope.launch {
            searchNewsRepository.getSearchForHeadLines(searchTerm)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }.collect{
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}