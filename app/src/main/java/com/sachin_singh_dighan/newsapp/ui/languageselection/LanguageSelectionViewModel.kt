package com.sachin_singh_dighan.newsapp.ui.languageselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.languageselection.LanguageData
import com.sachin_singh_dighan.newsapp.data.repository.languageselection.LanguageSelectionRepository
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LanguageSelectionViewModel(
    private val languageSelectionRepository: LanguageSelectionRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<LanguageData>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<LanguageData>>> = _uiState
    val languageCodeSet = mutableSetOf<String>()

    init {
        getLanguageData()
    }

    private fun getLanguageData() {
        viewModelScope.launch {
            if (networkHelper.isNetworkAvailable()) {
                languageSelectionRepository.getLanguageData()
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