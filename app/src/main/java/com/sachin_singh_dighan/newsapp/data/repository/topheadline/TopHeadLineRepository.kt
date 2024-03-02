package com.sachin_singh_dighan.newsapp.data.repository.topheadline

import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopHeadLineRepository @Inject constructor(private  val networkService: NetworkService){
    fun getTopHeadLines(country: String,language: String): Flow<List<Article>>{
        return flow {
            if (country.isNullOrEmpty()) {
                emit(networkService.getTopHeadLinesByLanguage(language))
            } else {
                emit(networkService.getTopHeadLinesByCountry(country))
            }
        }.map {
            it.articles
        }
    }
}