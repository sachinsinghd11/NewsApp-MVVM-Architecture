package com.sachin_singh_dighan.newsapp.data.model.topheadline

import com.google.gson.annotations.SerializedName
import com.sachin_singh_dighan.newsapp.data.local.entity.Article

data class ApiArticle(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("urlToImage")
    val imageUrl: String = "",
    @SerializedName("source")
    val apiSource: ApiSource
)

fun ApiArticle.toArticleEntity(): Article {
    return Article(
        title = this.title,
        description = this.description,
        url = this.url,
        imageUrl = this.imageUrl,
        source = this.apiSource.toSourceEntity()
    )
}
