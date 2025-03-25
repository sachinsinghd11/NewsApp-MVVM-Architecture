package com.sachin_singh_dighan.newsapp.data.repository.offlineArticles

import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.local.DatabaseService
import com.sachin_singh_dighan.newsapp.data.local.entity.Article
import com.sachin_singh_dighan.newsapp.data.model.topheadline.toArticleEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineArticleRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getArticles(country: String): Flow<List<Article>> {
        return flow {
            emit(networkService.getTopHeadLinesByCountry(country))
        }.map {
            it.apiArticles.map { apiArticle ->
                apiArticle.toArticleEntity()
            }
        }.flatMapLatest { articles ->
            flow {
                emit(databaseService.deleteAllAndInsertAll(articles))
            }
        }.flatMapLatest {
            databaseService.getArticles()
        }
    }

    fun getArticlesDirectlyFromDB(): Flow<List<Article>> {
        return databaseService.getArticles()
    }

}