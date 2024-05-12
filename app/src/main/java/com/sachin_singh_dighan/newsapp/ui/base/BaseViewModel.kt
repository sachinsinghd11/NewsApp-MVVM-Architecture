package com.sachin_singh_dighan.newsapp.ui.base

import androidx.lifecycle.ViewModel
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<T>(private val networkHelper: NetworkHelper) : ViewModel() {

    protected val _uiState = MutableStateFlow<UiState<T>>(UiState.Loading)

    val uiState: StateFlow<UiState<T>> = _uiState
    protected fun checkInternetConnection(): Boolean = networkHelper.isNetworkAvailable()

}