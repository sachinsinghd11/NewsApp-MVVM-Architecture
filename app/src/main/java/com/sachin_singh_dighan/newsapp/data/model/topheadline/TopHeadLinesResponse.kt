package com.sachin_singh_dighan.newsapp.data.model.topheadline

import com.google.gson.annotations.SerializedName

data class TopHeadLinesResponse(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("totalResults")
    val totalResults: Int = 0,
    @SerializedName("articles")
    val apiArticles: List<ApiArticle> = ArrayList(),
)
