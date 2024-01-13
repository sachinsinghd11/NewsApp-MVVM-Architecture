package com.sachin_singh_dighan.newsapp.ui.language_selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.data.model.country_selection.CountrySelection
import com.sachin_singh_dighan.newsapp.data.model.language_selection.LanguageData
import com.sachin_singh_dighan.newsapp.data.repository.country_selection.CountrySelectionRepository
import com.sachin_singh_dighan.newsapp.data.repository.language_selection.LanguageSelectionRepository
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LanguageSelectionViewModel(private val languageSelectionRepository: LanguageSelectionRepository): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<LanguageData>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<LanguageData>>> = _uiState

    init {
        getLanguageData()
    }

    private fun getLanguageData(){
        viewModelScope.launch {
            languageSelectionRepository.getLanguageData()
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

}