package com.sachin_singh_dighan.newsapp.data.repository.topheadline

import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiArticle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopHeadLineRepository @Inject constructor(
    private val networkService: NetworkService,
) {

    fun getTopHeadLinesByDefault(): Flow<List<ApiArticle>> {
        return flow {
            emit(networkService.getTopHeadLinesByCountry(AppConstant.NEWS_BY_DEFAULT))
        }.map {
            it.apiArticles
        }
    }

    fun getTopHeadLinesByCategory(category: String): Flow<List<ApiArticle>> {
        return flow {
            emit(networkService.getTopHeadLinesByCategory(category))
        }.map {
            it.apiArticles
        }
    }

    fun getTopHeadLinesByCountry(country: String): Flow<List<ApiArticle>> {
        return flow {
            emit(networkService.getTopHeadLinesByCountry(country))
        }.map {
            it.apiArticles
        }
    }

    fun getTopHeadLinesByLanguage(language: String): Flow<List<ApiArticle>> {
        return flow {
            emit(networkService.getTopHeadLinesByLanguage(language))
        }.map {
            it.apiArticles
        }
    }
}