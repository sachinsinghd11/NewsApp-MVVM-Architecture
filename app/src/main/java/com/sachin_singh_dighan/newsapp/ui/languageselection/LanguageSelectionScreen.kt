package com.sachin_singh_dighan.newsapp.ui.languageselection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sachin_singh_dighan.newsapp.data.model.languageselection.LanguageData
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.component.NewsTopAppBar
import com.sachin_singh_dighan.newsapp.ui.component.ShowError
import com.sachin_singh_dighan.newsapp.ui.component.ShowLoading

@Composable
fun LanguageSelectionRoute(
    onClick: (List<String>) -> Unit,
    viewModel: LanguageSelectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NewsTopAppBar()
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LanguageSelectionScreen(uiState, viewModel, onClick = onClick)
        }
    }
}

@Composable
fun LanguageSelectionScreen(
    uiState: UiState<List<LanguageData>>,
    viewModel: LanguageSelectionViewModel,
    onClick: (List<String>) -> Unit
) {

    when (uiState) {
        is UiState.Success -> {
            LanguageList(uiState.data, viewModel, onClick)
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
fun LanguageList(
    list: List<LanguageData>,
    viewModel: LanguageSelectionViewModel,
    onClick: (List<String>) -> Unit
) {


    LazyColumn(Modifier.padding(8.dp)) {
        items(list) { language ->
            Language(
                language = language,
                isSelected = viewModel.isItemSelected(language.languageCode),
                viewModel = viewModel,
                onItemClick = {
                    val shouldNavigate = viewModel.toggleSelection(language.languageCode)
                    if (shouldNavigate) {
                        // If this was the second selection, navigate immediately
                        onClick(viewModel.selectedItems)
                    }
                }
            )
        }
    }
}

@Composable
fun Language(
    language: LanguageData,
    isSelected: Boolean,
    viewModel: LanguageSelectionViewModel,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .height(70.dp)
            .padding(8.dp)
            .background(
                if (viewModel.selectedItems.size <= 2 && isSelected) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(4.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = language.languageName,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}
