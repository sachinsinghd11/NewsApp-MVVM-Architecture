package com.sachin_singh_dighan.newsapp.ui.countryselection

import android.annotation.SuppressLint
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
import com.sachin_singh_dighan.newsapp.data.model.countryselection.CountrySelection
import com.sachin_singh_dighan.newsapp.ui.component.ShowError
import com.sachin_singh_dighan.newsapp.ui.component.ShowLoading
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.component.ButtonMainSection
import com.sachin_singh_dighan.newsapp.ui.component.NewsTopAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CountrySelectionRoute(
    onClick: (String) -> Unit,
    viewModel: CountrySelectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            NewsTopAppBar()
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            CountrySelectionScreen(uiState, onClick = onClick)
        }
    }
}

@Composable
fun CountrySelectionScreen(
    uiState: UiState<List<CountrySelection>>,
    onClick: (String) -> Unit
) {
    when (uiState) {
        is UiState.Success -> {
            CountryList(uiState.data, onClick)
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
fun CountryList(list: List<CountrySelection>, onClick: (String) -> Unit) {
    LazyColumn(Modifier.padding(8.dp)) {
        items(list) { country ->
            Country(country, onClick)
        }
    }
}

@Composable
fun Country(country: CountrySelection, onClick: (String) -> Unit) {
    ButtonMainSection(onClick, data = country)
}
