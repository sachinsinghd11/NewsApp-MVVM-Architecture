package com.sachin_singh_dighan.newsapp.ui.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.sachin_singh_dighan.newsapp.utils.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiArticle
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.component.BannerImage
import com.sachin_singh_dighan.newsapp.ui.component.DescriptionText
import com.sachin_singh_dighan.newsapp.ui.component.NewsTopAppBar
import com.sachin_singh_dighan.newsapp.ui.component.ShowError
import com.sachin_singh_dighan.newsapp.ui.component.ShowLoading
import com.sachin_singh_dighan.newsapp.ui.component.SourceText
import com.sachin_singh_dighan.newsapp.ui.component.TitleText

@Composable
fun NewsListRoute(
    fetchNewByType: String = "",
    selectedLanguages: List<String> = emptyList(),
    newType: String,
    onNewsClick: (url: String) -> Unit,
    viewModel: NewsListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (newType == AppConstant.NEWS_BY_CATEGORY_VALUE) {
        viewModel.fetchNewsByCategory(fetchNewByType)
    } else if (newType == AppConstant.NEWS_BY_COUNTRY_VALUE) {
        viewModel.fetchNewsByCountry(fetchNewByType)
    } else if (newType == AppConstant.NEWS_BY_LANGUAGE_VALUE) {
        viewModel.fetchNewsByLanguage(selectedLanguages)
    }


    Scaffold(
        topBar = {
            NewsTopAppBar()
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            NewsListScreen(uiState, onNewsClick = onNewsClick)
        }
    }
}

@Composable
fun NewsListScreen(
    uiState: UiState<List<ApiArticle>>,
    onNewsClick: (url: String) -> Unit
) {
    when (uiState) {
        is UiState.Success -> {
            NewsListList(uiState.data, onNewsClick)
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
fun NewsListList(list: List<ApiArticle>, onNewsClick: (url: String) -> Unit) {
    LazyColumn(Modifier.padding(8.dp)) {
        items(list) { article ->
            Article(article, onNewsClick)
        }
    }
}

@Composable
fun Article(apiArticle: ApiArticle, onNewsClick: (url: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (apiArticle.url.isNotEmpty()) {
                    onNewsClick(apiArticle.url)
                }
            }
    ) {
        BannerImage(apiArticle)
        TitleText(apiArticle.title)
        DescriptionText(apiArticle.description)
        SourceText(apiArticle.apiSource)
    }
}