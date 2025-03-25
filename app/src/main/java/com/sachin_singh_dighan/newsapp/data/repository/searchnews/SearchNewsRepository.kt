package com.sachin_singh_dighan.newsapp.data.repository.searchnews

import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiArticle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchNewsRepository @Inject constructor(private  val networkService: NetworkService) {

    fun getSearchForHeadLines(searchTerm: String): Flow<List<ApiArticle>> {
        return flow {
            emit(networkService.getSearchResultForHeadLines(searchTerm))
        }.map {
            it.apiArticles
        }
    }
}