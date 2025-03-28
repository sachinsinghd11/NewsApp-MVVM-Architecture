package com.sachin_singh_dighan.newsapp.ui.topheadlinebypaging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiArticle
import com.sachin_singh_dighan.newsapp.data.repository.topheadlinepaging.TopHeadlinePagingSourceRepository
import com.sachin_singh_dighan.newsapp.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopHeadlineByPagingViewModel @Inject constructor(
    private val topHeadlinePagingSourceRepository: TopHeadlinePagingSourceRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {
    private val _uiState = MutableStateFlow<PagingData<ApiArticle>>(value = PagingData.empty())
    val uiState: StateFlow<PagingData<ApiArticle>> = _uiState

    init {
        getNewsByPagination()
    }

    private fun getNewsByPagination() {
        viewModelScope.launch(dispatcherProvider.main) {
            topHeadlinePagingSourceRepository.getTopHeadlineByPagination()
                .collect {
                    _uiState.value = it
                }
        }
    }
}