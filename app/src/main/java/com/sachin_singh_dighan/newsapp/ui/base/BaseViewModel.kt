package com.sachin_singh_dighan.newsapp.ui.base

import androidx.lifecycle.ViewModel
import com.sachin_singh_dighan.newsapp.utils.AppConstant
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException

abstract class BaseViewModel<T>(private val networkHelper: NetworkHelper) : ViewModel() {

    protected val _uiState = MutableStateFlow<UiState<T>>(UiState.Loading)

    val uiState: StateFlow<UiState<T>> = _uiState

    // Track if an error dialog is showing
    val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog

    // Store error message
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage


    protected fun checkInternetConnection(): Boolean = networkHelper.isNetworkAvailable()

    // Handle errors
    fun handleError(exception: Throwable) {
        val errorMsg = when (exception) {
            is IOException -> AppConstant.NETWORK_ERROR
            else -> exception.message ?: "An unknown error occurred"
        }

        _uiState.value = UiState.Error(errorMsg)
        _errorMessage.value = errorMsg
        _showErrorDialog.value = true
    }

    // Dismiss the error dialog without retrying
    fun dismissErrorDialog() {
        _showErrorDialog.value = false
    }

}