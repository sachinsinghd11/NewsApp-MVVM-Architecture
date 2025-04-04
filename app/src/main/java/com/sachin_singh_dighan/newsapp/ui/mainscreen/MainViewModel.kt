package com.sachin_singh_dighan.newsapp.ui.mainscreen

import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.data.model.mainscreen.MainSection
import com.sachin_singh_dighan.newsapp.data.repository.mainscreen.MainRepository
import com.sachin_singh_dighan.newsapp.ui.base.BaseViewModel
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.news.NewsListViewModel
import com.sachin_singh_dighan.newsapp.utils.DispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    networkHelper: NetworkHelper,
    private val logger: Logger,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<List<MainSection>>(networkHelper) {

    init {
        getMainSections()
    }

    private fun getMainSections() {
        viewModelScope.launch(dispatcherProvider.main) {
            mainRepository.getDataForMainSection()
                .flowOn(dispatcherProvider.default)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                    logger.d(NewsListViewModel.TAG, e.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                    logger.d(NewsListViewModel.TAG, it.toString())
                }
        }
    }
}