package com.sachin_singh_dighan.newsapp.data.local

import com.sachin_singh_dighan.newsapp.data.local.entity.Article
import kotlinx.coroutines.flow.Flow


interface DatabaseService {
    fun getArticles(): Flow<List<Article>>

    fun deleteAllAndInsertAll(apiArticles: List<Article>)
}