package com.sachin_singh_dighan.newsapp.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiArticle

@Composable
fun BannerImage(
    apiArticle: ApiArticle
){
    AsyncImage(
        model = apiArticle.imageUrl,
        contentDescription = apiArticle.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
    )
}