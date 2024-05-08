package com.sachin_singh_dighan.newsapp.data.repository.news

import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsListRepository @Inject constructor(private val networkService: NetworkService) {
    fun getNewsListByResources(sourceId: String): Flow<List<Article>> {
        return flow {
            emit(networkService.getNewsByResources(sourceId))
        }.map {
            it.articles
        }
    }
}