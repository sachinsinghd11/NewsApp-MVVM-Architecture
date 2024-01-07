package com.sachin_singh_dighan.newsapp.data.repository.top_headline

import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.model.top_headline.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopHeadLineRepository @Inject constructor(private  val networkService: NetworkService){
    fun getTopHeadLines(country: String): Flow<List<Article>>{
        return flow {
            emit(networkService.getTopHeadLines(country))
        }.map {
            it.articles
        }
    }
}