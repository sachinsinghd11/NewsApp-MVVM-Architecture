package com.sachin_singh_dighan.newsapp.ui.languageselection

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.languageselection.LanguageData
import com.sachin_singh_dighan.newsapp.data.repository.languageselection.LanguageSelectionRepository
import com.sachin_singh_dighan.newsapp.ui.base.BaseViewModel
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.DispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageSelectionViewModel @Inject constructor(
    private val languageSelectionRepository: LanguageSelectionRepository,
    private val networkHelper: NetworkHelper,
    private val logger: Logger,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<List<LanguageData>>(networkHelper) {

    companion object {
        const val TAG = "LanguageSelectionViewModel"
    }

    private val _selectedItems = mutableStateListOf<String>()
    val selectedItems: List<String> = _selectedItems

    init {
        getLanguageData()
    }

    private fun getLanguageData() {
        viewModelScope.launch(dispatcherProvider.main) {
            if (networkHelper.isNetworkAvailable()) {
                languageSelectionRepository.getLanguageData()
                    .flowOn(dispatcherProvider.default)
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

    // Returns true if this selection triggered navigation (second item selected)
    fun toggleSelection(item: String): Boolean {
        if (_selectedItems.contains(item)) {
            _selectedItems.remove(item)
            return false
        } else {
            if (_selectedItems.size <= 1) {
                _selectedItems.add(item)
                return _selectedItems.size == 2 // Return true if this was the second selection
            } else {
                return false // Return false if this was the 3rd selection
            }

        }
    }

    fun clearSelection() {
        _selectedItems.clear()
    }

    fun isItemSelected(item: String): Boolean {
        return _selectedItems.contains(item)
    }

}