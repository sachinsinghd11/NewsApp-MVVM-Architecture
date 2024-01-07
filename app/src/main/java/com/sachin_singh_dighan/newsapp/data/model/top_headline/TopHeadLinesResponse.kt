package com.sachin_singh_dighan.newsapp.data.model.top_headline

import com.google.gson.annotations.SerializedName

data class TopHeadLinesResponse(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("totalResults")
    val totalResults: Int = 0,
    @SerializedName("articles")
    val articles: List<Article> = ArrayList(),
)
