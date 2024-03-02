package com.sachin_singh_dighan.newsapp.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.data.model.mainscreen.MainSection
import com.sachin_singh_dighan.newsapp.data.repository.mainscreen.MainRepository
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<MainSection>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<MainSection>>> = _uiState

    init {
        getMainSections()
    }

    private fun getMainSections(){
        viewModelScope.launch {
            mainRepository.getDataForMainSection()
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}