package com.sachin_singh_dighan.newsapp.data.repository.topheadline

import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopHeadLineRepository @Inject constructor(
    private val networkService: NetworkService,
) {

    fun getTopHeadLinesByDefault(): Flow<List<Article>> {
        return flow {
            emit(networkService.getTopHeadLinesByCountry(AppConstant.NEWS_BY_DEFAULT))
        }.map {
            it.articles
        }
    }

    fun getTopHeadLinesByCountry(country: String): Flow<List<Article>> {
        return flow {
            emit(networkService.getTopHeadLinesByCountry(country))
        }.map {
            it.articles
        }
    }

    fun getTopHeadLinesByLanguage(language: String): Flow<List<Article>> {
        return flow {
            emit(networkService.getTopHeadLinesByLanguage(language))
        }.map {
            it.articles
        }
    }
}