package com.sachin_singh_dighan.newsapp.ui.languageselection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sachin_singh_dighan.newsapp.data.model.languageselection.LanguageData
import com.sachin_singh_dighan.newsapp.ui.component.ShowError
import com.sachin_singh_dighan.newsapp.ui.component.ShowLoading
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.component.ButtonMainSection
import com.sachin_singh_dighan.newsapp.ui.component.NewsTopAppBar

@Composable
fun LanguageSelectionRoute(
    onClick: (String) -> Unit,
    viewModel: LanguageSelectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            NewsTopAppBar()
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LanguageSelectionScreen(uiState, onClick = onClick)
        }
    }
}

@Composable
fun LanguageSelectionScreen(
    uiState: UiState<List<LanguageData>>,
    onClick: (String) -> Unit
) {
    when (uiState) {
        is UiState.Success -> {
            LanguageList(uiState.data, onClick)
        }

        is UiState.Loading -> {
            ShowLoading()
        }

        is UiState.Error -> {
            ShowError(uiState.message)
        }

    }
}

@Composable
fun LanguageList(list: List<LanguageData>, onClick: (String) -> Unit) {
    LazyColumn(Modifier.padding(8.dp)) {
        items(list) { country ->
            Language(country, onClick)
        }
    }
}

@Composable
fun Language(language: LanguageData, onClick: (String) -> Unit) {
    ButtonMainSection(onClick, data = language)
}