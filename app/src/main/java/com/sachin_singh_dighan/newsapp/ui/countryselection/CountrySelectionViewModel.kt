package com.sachin_singh_dighan.newsapp.ui.countryselection

import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.repository.countryselection.CountrySelectionRepository
import com.sachin_singh_dighan.newsapp.ui.base.BaseViewModel
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountrySelectionViewModel @Inject constructor(
    private val countrySelectionRepository: CountrySelectionRepository,
    private val networkHelper: NetworkHelper,
    private val logger: Logger,
) : BaseViewModel<List<*>>(networkHelper) {

    companion object {
        const val TAG = "CountrySelectionViewModel"
    }

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