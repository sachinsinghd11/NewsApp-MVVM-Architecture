package com.sachin_singh_dighan.newsapp.data.repository.search_news

import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.model.top_headline.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchNewsRepository @Inject constructor(private  val networkService: NetworkService) {

    fun getSearchForHeadLines(searchTerm: String): Flow<List<Article>> {
        return flow {
            emit(networkService.getSearchResultForHeadLines(searchTerm))
        }.map {
            it.articles
        }
    }
}