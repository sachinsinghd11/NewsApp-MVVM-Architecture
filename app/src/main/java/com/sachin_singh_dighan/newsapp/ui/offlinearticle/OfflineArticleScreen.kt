package com.sachin_singh_dighan.newsapp.ui.offlinearticle

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.sachin_singh_dighan.newsapp.data.local.entity.Article
import com.sachin_singh_dighan.newsapp.data.local.entity.Source
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.component.NewsTopAppBar
import com.sachin_singh_dighan.newsapp.ui.component.ShowError
import com.sachin_singh_dighan.newsapp.ui.component.ShowLoading


@Composable
fun OfflineArticleRoute(
    onNewsClick: (url: String) -> Unit,
    viewModel: OfflineArticleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            NewsTopAppBar()
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            OfflineArticleScreen(uiState = uiState, onNewsClick = onNewsClick)
        }

    }

}

@Composable
fun OfflineArticleScreen(uiState: UiState<List<Article>>, onNewsClick: (url: String) -> Unit) {
    when (uiState) {
        is UiState.Success -> {
            ArticleList(uiState.data, onNewsClick)
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
fun ArticleList(articles: List<Article>, onNewsClick: (url: String) -> Unit) {
    LazyColumn {
        items(articles, key = { article -> article.url }) { article ->
            Article(article, onNewsClick)
        }
    }
}

@Composable
fun Article(article: Article, onNewsClick: (url: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (article.url.isNotEmpty()) {
                    onNewsClick(article.url)
                }
            }) {
        BannerImage(article)
        TitleText(article.title)
        DescriptionText(article.description)
        SourceText(article.source)
    }

}

@Composable
fun BannerImage(article: Article) {
    AsyncImage(
        model = article.imageUrl,
        contentDescription = article.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
    )
}

@Composable
fun TitleText(title: String) {
    if (title.isNotEmpty()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            maxLines = 2,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun DescriptionText(description: String?) {
    if (!description.isNullOrEmpty()) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            maxLines = 2,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun SourceText(source: Source) {
    Text(
        text = source.name,
        style = MaterialTheme.typography.titleSmall,
        color = Color.Gray,
        maxLines = 1,
        modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)
    )
}
