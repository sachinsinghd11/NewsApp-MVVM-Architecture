package com.sachin_singh_dighan.newsapp.ui.countryselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.countryselection.CountrySelection
import com.sachin_singh_dighan.newsapp.data.repository.countryselection.CountrySelectionRepository
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.news.NewsListViewModel
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CountrySelectionViewModel(
    private val countrySelectionRepository: CountrySelectionRepository,
    private val networkHelper: NetworkHelper,
    private val logger: Logger,
) : ViewModel() {

    companion object {
        const val TAG = "CountrySelectionViewModel"
    }

    private val _uiState = MutableStateFlow<UiState<List<CountrySelection>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<CountrySelection>>> = _uiState

    init {
        getCountrySelection()
    }

    private fun getCountrySelection() {
        viewModelScope.launch {
            if (networkHelper.isNetworkAvailable()) {
                countrySelectionRepository.getDataForCountry()
                    .flowOn(Dispatchers.Default)
                    .catch { e ->
                        _uiState.value = UiState.Error(e.toString())
                        logger.d(NewsListViewModel.TAG, e.toString())
                    }.collect {
                        _uiState.value = UiState.Success(it)
                        logger.d(NewsListViewModel.TAG, it.toString())
                    }
            } else {
                _uiState.value = UiState.Error(AppConstant.NETWORK_ERROR)
            }

        }
    }

}