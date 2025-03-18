package com.sachin_singh_dighan.newsapp.ui.newsources

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
import com.sachin_singh_dighan.newsapp.data.model.newsources.Sources
import com.sachin_singh_dighan.newsapp.ui.component.ShowError
import com.sachin_singh_dighan.newsapp.ui.component.ShowLoading
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.component.ButtonMainSection
import com.sachin_singh_dighan.newsapp.ui.component.NewsTopAppBar

@Composable
fun NewsSourceRoute(
    onClick: (url: String) -> Unit,
    viewModel: NewsSourcesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NewsTopAppBar()
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            NesSourceScreen(uiState, onClick = onClick)
        }
    }
}


@Composable
fun NesSourceScreen(
    uiState: UiState<List<Sources>>,
    onClick: (url: String) -> Unit
) {
    when (uiState) {
        is UiState.Success -> {
            NewsSourceList(uiState.data, onClick)
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
fun NewsSourceList(list: List<Sources>, onClick: (url: String) -> Unit) {
    LazyColumn(Modifier.padding(8.dp)) {
        items(list) { section ->
            NewsSource(section, onClick)
        }
    }
}

@Composable
fun NewsSource(sources: Sources, onClick: (url: String) -> Unit) {
    ButtonMainSection(onClick, data = sources)
}
